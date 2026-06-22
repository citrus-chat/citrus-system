package com.javaee2026.citruschat.identity.infrastructure.storage;

import com.javaee2026.citruschat.identity.application.exceptions.InvalidAvatarException;
import com.javaee2026.citruschat.identity.application.ports.IUserAvatarStorage;
import com.javaee2026.citruschat.identity.application.results.StoredAvatarResult;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class LocalUserAvatarStorage implements IUserAvatarStorage {

	private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp",
			"image/gif");

	private static final Map<String, String> EXTENSIONS_BY_CONTENT_TYPE = Map.of("image/jpeg", ".jpg", "image/png",
			".png", "image/webp", ".webp", "image/gif", ".gif");

	private final Path storageDirectory;
	private final long maxSizeBytes;

	public LocalUserAvatarStorage(@Value("${citrus.avatar.storage-dir:uploads/avatars}") String storageDirectory,
			@Value("${citrus.avatar.max-size-bytes:5242880}") long maxSizeBytes) {
		this.storageDirectory = Paths.get(storageDirectory).toAbsolutePath().normalize();
		this.maxSizeBytes = maxSizeBytes;
	}

	@Override
	public StoredAvatarResult store(UUID userId, String originalFilename, String contentType, byte[] content) {
		validate(contentType, content);

		try {
			Files.createDirectories(storageDirectory);

			String filename = userId + "-" + UUID.randomUUID() + EXTENSIONS_BY_CONTENT_TYPE.get(contentType);
			Path target = storageDirectory.resolve(filename).normalize();

			if (!target.startsWith(storageDirectory)) {
				throw new InvalidAvatarException("Invalid avatar filename");
			}

			Files.write(target, content, StandardOpenOption.CREATE_NEW);

			return new StoredAvatarResult(ApiRoutes.API_USER_AVATAR_IMAGE_BASE + "/" + filename);
		} catch (IOException ex) {
			throw new InvalidAvatarException("Avatar could not be stored");
		}
	}

	@Override
	public void deleteByUrl(String avatarUrl) {
		String filename = filenameFromUrl(avatarUrl);

		if (filename == null) {
			return;
		}

		try {
			Path target = resolveFilename(filename);
			Files.deleteIfExists(target);
		} catch (IOException ex) {
			throw new InvalidAvatarException("Avatar could not be deleted");
		}
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = resolveFilename(filename);
			Resource resource = new UrlResource(file.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				throw new InvalidAvatarException("Avatar image was not found");
			}

			return resource;
		} catch (MalformedURLException ex) {
			throw new InvalidAvatarException("Invalid avatar filename");
		}
	}

	public String contentType(String filename) {
		String lowerFilename = filename.toLowerCase();

		if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		if (lowerFilename.endsWith(".png")) {
			return "image/png";
		}
		if (lowerFilename.endsWith(".webp")) {
			return "image/webp";
		}
		if (lowerFilename.endsWith(".gif")) {
			return "image/gif";
		}

		return "application/octet-stream";
	}

	private void validate(String contentType, byte[] content) {
		if (content == null || content.length == 0) {
			throw new InvalidAvatarException("Avatar file is required");
		}
		if (content.length > maxSizeBytes) {
			throw new InvalidAvatarException("Avatar file is too large");
		}
		if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
			throw new InvalidAvatarException("Avatar must be a JPEG, PNG, WebP, or GIF image");
		}
		if (!hasExpectedSignature(contentType, content)) {
			throw new InvalidAvatarException("Avatar file content does not match its image type");
		}
	}

	private boolean hasExpectedSignature(String contentType, byte[] content) {
		return switch (contentType) {
			case "image/jpeg" -> content.length >= 3 && (content[0] & 0xff) == 0xff && (content[1] & 0xff) == 0xd8
					&& (content[2] & 0xff) == 0xff;
			case "image/png" -> content.length >= 8 && (content[0] & 0xff) == 0x89 && content[1] == 0x50
					&& content[2] == 0x4e && content[3] == 0x47 && content[4] == 0x0d && content[5] == 0x0a
					&& content[6] == 0x1a && content[7] == 0x0a;
			case "image/webp" -> content.length >= 12 && content[0] == 0x52 && content[1] == 0x49 && content[2] == 0x46
					&& content[3] == 0x46 && content[8] == 0x57 && content[9] == 0x45 && content[10] == 0x42
					&& content[11] == 0x50;
			case "image/gif" -> content.length >= 6 && content[0] == 0x47 && content[1] == 0x49 && content[2] == 0x46
					&& content[3] == 0x38 && (content[4] == 0x37 || content[4] == 0x39) && content[5] == 0x61;
			default -> false;
		};
	}

	private Path resolveFilename(String filename) {
		if (filename == null || filename.isBlank() || filename.contains("/") || filename.contains("\\")) {
			throw new InvalidAvatarException("Invalid avatar filename");
		}

		Path target = storageDirectory.resolve(filename).normalize();

		if (!target.startsWith(storageDirectory)) {
			throw new InvalidAvatarException("Invalid avatar filename");
		}

		return target;
	}

	private String filenameFromUrl(String avatarUrl) {
		if (avatarUrl == null || avatarUrl.isBlank()) {
			return null;
		}

		String prefix = ApiRoutes.API_USER_AVATAR_IMAGE_BASE + "/";
		if (!avatarUrl.startsWith(prefix)) {
			return null;
		}

		return avatarUrl.substring(prefix.length());
	}
}

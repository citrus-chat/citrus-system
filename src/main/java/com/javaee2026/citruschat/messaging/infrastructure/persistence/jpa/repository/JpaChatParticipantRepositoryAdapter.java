// package
// com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;
//
// import
// com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
// import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
// import
// com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatParticipantMapper;
// import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
// import jakarta.transaction.Transactional;
//
// public class JpaChatParticipantRepositoryAdapter implements
// IChatParticipantRepository {
//
// private final SpringDataChatParticipantRepository chatParticipantRepository;
// private final ChatParticipantMapper chatParticipantMapper;
//
// public
// JpaChatParticipantRepositoryAdapter(SpringDataChatParticipantRepository
// chatParticipantRepository,
// ChatParticipantMapper chatParticipantMapper) {
// this.chatParticipantRepository = chatParticipantRepository;
// this.chatParticipantMapper = chatParticipantMapper;
// }
//
// @Override
// @Transactional
// public void save(ChatParticipant chatParticipant) {
// chatParticipantRepository.save(chatParticipantMapper.toJpa(chatParticipant));
// }
//
// public boolean existsDirectChat(UserId user1, UserId user2) {
// return true; // <- True for now.
// }
// }

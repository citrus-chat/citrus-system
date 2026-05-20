// package
// com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;
//
// import
// com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
// import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
// import
// com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoleMapper;
// import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
// import jakarta.transaction.Transactional;
//
// import java.util.List;
// import java.util.Optional;
//
// public class JpaChatRoleRepositoryAdapter implements IChatRoleRepository {
//
// private final SpringDataChatRoleRepository chatRoleRepository;
// private final ChatRoleMapper chatRoleMapper;
//
// public JpaChatRoleRepositoryAdapter(SpringDataChatRoleRepository
// chatRoleRepository, ChatRoleMapper chatRoleMapper) {
// this.chatRoleRepository = chatRoleRepository;
// this.chatRoleMapper = chatRoleMapper;
// }
//
// @Override
// @Transactional
// public void save(ChatRole chatRole) {
// chatRoleRepository.save(chatRoleMapper.toJpa(chatRole));
// }
//
// @Override
// public Optional<ChatRole> findById(RoleId id) {
// return chatRoleRepository.findById(id.value()).map(chatRoleMapper::toDomain);
// }
//
// @Override
// public List<ChatRole> findGlobalRoles() {
// return
// chatRoleRepository.findByChatRoomIdIsNull().stream().map(chatRoleMapper::toDomain).toList();
// }
// @Override
// public Optional<ChatRole> ownerRole(){
// return findGlobalRoles().stream().filter(role ->
// role.getName().equals("OWNER")).findFirst();
// }
//
// @Override
// public Optional<ChatRole> memberRole(){
// return findGlobalRoles().stream().filter(role ->
// role.getName().equals("Member")).findFirst();
// }
// }

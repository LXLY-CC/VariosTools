package com.varios.toolshub.tools.vault;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaultEntryRepository extends JpaRepository<VaultEntry, Long> {
    List<VaultEntry> findByUserIdAndWebsiteUrlContainingIgnoreCaseOrUserIdAndUsernameContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            Long userId1, String websiteUrl,
            Long userId2, String username,
            Long userId3, String description);

    List<VaultEntry> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<VaultEntry> findByIdAndUserId(Long id, Long userId);
}

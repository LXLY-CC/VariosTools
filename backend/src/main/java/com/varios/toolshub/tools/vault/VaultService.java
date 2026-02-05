package com.varios.toolshub.tools.vault;

import com.varios.toolshub.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VaultService {
    private final VaultEntryRepository repository;
    private final UserRepository userRepository;
    private final VaultCryptoService cryptoService;

    public VaultService(VaultEntryRepository repository, UserRepository userRepository, VaultCryptoService cryptoService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cryptoService = cryptoService;
    }

    public List<VaultDtos.VaultEntryResponse> list(Long userId, String search) {
        List<VaultEntry> entries;
        if (search == null || search.isBlank()) {
            entries = repository.findByUserIdOrderByCreatedAtDesc(userId);
        } else {
            entries = repository.findByUserIdAndWebsiteUrlContainingIgnoreCaseOrUserIdAndUsernameContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                    userId, search, userId, search, userId, search);
        }
        return entries.stream().map(this::toResponse).toList();
    }

    @Transactional
    public void create(Long userId, VaultDtos.VaultUpsertRequest request) {
        VaultEntry entry = new VaultEntry();
        entry.setUser(userRepository.getReferenceById(userId));
        fill(entry, request);
        repository.save(entry);
    }

    @Transactional
    public void update(Long userId, Long id, VaultDtos.VaultUpsertRequest request) {
        VaultEntry entry = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new EntityNotFoundException("Vault entry not found"));
        fill(entry, request);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        VaultEntry entry = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new EntityNotFoundException("Vault entry not found"));
        repository.delete(entry);
    }

    private void fill(VaultEntry entry, VaultDtos.VaultUpsertRequest request) {
        entry.setWebsiteUrl(request.websiteUrl());
        entry.setUsername(request.username());
        entry.setDescription(request.description());
        entry.setPasswordEncrypted(cryptoService.encrypt(request.password()));
    }

    private VaultDtos.VaultEntryResponse toResponse(VaultEntry e) {
        return new VaultDtos.VaultEntryResponse(e.getId(), e.getWebsiteUrl(), e.getUsername(), cryptoService.decrypt(e.getPasswordEncrypted()),
                e.getDescription(), e.getCreatedAt(), e.getUpdatedAt());
    }
}

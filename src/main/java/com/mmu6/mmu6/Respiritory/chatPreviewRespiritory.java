package com.mmu6.mmu6.Respiritory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.Chat;
import com.mmu6.mmu6.Class.ChatPreview;


public interface chatPreviewRespiritory extends JpaRepository<ChatPreview, Long> {
	
	

}

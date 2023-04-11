package com.mmu6.mmu6.Respiritory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.Chat;


public interface chatRespiritory extends JpaRepository<Chat, Long> {

}

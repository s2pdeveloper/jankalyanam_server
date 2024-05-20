package com.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.GENDER;
import com.app.constant.ServiceConstant.PROVIDED;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor 
@Getter
@Setter
@Entity
@Table (name = "ChatMessage")
public class ChatMessageDO {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column
    private String room;
    
    @Column(name = "sender_id")
    private Long senderId;
    
    @Column(name = "receiver_id")
    private Long receiverId;
    
    @Column
    private String message;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;


}

package com.shop.bike.service;

import com.shop.bike.consumer.vm.MessageVM;
import com.shop.bike.service.dto.MessageDTO;
import com.shop.bike.service.dto.MessagePojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    Page<MessageVM> getAllMessage(String chatId,Pageable pageable);

    void sendMessageByConsumer(MessageDTO dto);

    Page<MessagePojo> findAllChannel(Pageable pageable);

}
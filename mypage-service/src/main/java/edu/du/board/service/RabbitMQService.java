package edu.du.board.service;

import edu.du.board.config.RabbitMQConfig;
import edu.du.board.model.Board;
import edu.du.board.model.OrderCompleteMessage;
import edu.du.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final BoardRepository boardRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BOARD, containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    public void receiveMessage(OrderCompleteMessage message) {
        System.out.println("==========================================");
        System.out.println("RabbitMQ 메시지 수신 시작");
        System.out.println("==========================================");
        
        System.out.println("수신된 메시지 내용:");
        System.out.println("------------------------------------------");
        System.out.println("주문 ID: " + message.getOrderId());
        System.out.println("상품명: " + message.getProductName());
        System.out.println("수량: " + message.getQuantity());
        System.out.println("고객명: " + message.getCustomerName());
        System.out.println("주문일시: " + message.getOrderDate());
        System.out.println("------------------------------------------");

        try {
            System.out.println("게시판 저장 시작...");
            // 게시판에 주문 완료 메시지 저장
            Board board = new Board();
            board.setTitle("주문 완료: " + message.getProductName());
            board.setContent(String.format("주문 ID: %d\n상품명: %s\n수량: %d\n고객명: %s\n주문일시: %s",
                    message.getOrderId(),
                    message.getProductName(),
                    message.getQuantity(),
                    message.getCustomerName(),
                    message.getOrderDate()));
            board.setAuthor(message.getCustomerName());
            board.setCreatedAt(LocalDateTime.now());
            board.setCategory("주문완료");
            board.setOrderId(message.getOrderId());

            Board savedBoard = boardRepository.save(board);
            System.out.println("게시판 저장 완료! 저장된 게시글 ID: " + savedBoard.getId());
            System.out.println("==========================================");
        } catch (Exception e) {
            System.err.println("게시판 저장 중 오류 발생!");
            System.err.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 
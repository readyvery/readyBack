package com.readyvery.readyverydemo.src.board;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.repository.BoardRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.src.board.dto.BoardMapper;
import com.readyvery.readyverydemo.src.board.dto.BoardRes;
import com.readyvery.readyverydemo.src.board.dto.BoardSearchRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final BoardMapper boardMapper;

	@Override
	public BoardRes getStoreList() {
		List<Store> boards = boardRepository.findAll();
		if (boards.isEmpty()) {
			throw new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND);
		}
		return boardMapper.toBoardRes(boards);
	}

	@Override
	public BoardSearchRes getSearchList() {
		List<Store> boards = boardRepository.findAll();
		if (boards.isEmpty()) {
			throw new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND);
		}
		return boardMapper.toBoardSearchRes(boards);
	}
}

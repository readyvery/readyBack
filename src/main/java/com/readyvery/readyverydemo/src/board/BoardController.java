package com.readyvery.readyverydemo.src.board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.src.board.dto.BoardRes;
import com.readyvery.readyverydemo.src.board.dto.BoardSearchRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/board")
public class BoardController {

	private final BoardService boardServiceImpl;

	@GetMapping("/store")
	public BoardRes getStoreList() {
		return boardServiceImpl.getStoreList();
	}

	@GetMapping("/search")
	public BoardSearchRes getSearchList() {
		return boardServiceImpl.getSearchList();
	}
}

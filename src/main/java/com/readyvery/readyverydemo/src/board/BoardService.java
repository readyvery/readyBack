package com.readyvery.readyverydemo.src.board;

import com.readyvery.readyverydemo.src.board.dto.BoardRes;
import com.readyvery.readyverydemo.src.board.dto.BoardSearchRes;

public interface BoardService {
	BoardRes getStoreList();

	BoardSearchRes getSearchList();
}

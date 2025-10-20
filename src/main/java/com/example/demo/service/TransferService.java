package com.example.demo.service;

import com.example.demo.dtos.request.TransferRequest;
import com.example.demo.dtos.response.TransferResponse;

public interface TransferService {
	public TransferResponse transfer(TransferRequest request, String username);

	public boolean validateTransfer(TransferRequest request, String username);
}

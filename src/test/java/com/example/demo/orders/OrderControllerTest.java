package com.example.demo.orders;

import com.example.demo.shared.LineItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@DisabledInAotMode
@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private Orders orders;

	private OrderPlacedCommand command;

	@BeforeEach
	void setUp() {
		command = new OrderPlacedCommand(Set.of(new LineItemDto("SKU123", 2)));
	}

	@Test
	void shouldPlaceOrderSuccessfully() throws Exception {
		// Given
		String jsonRequest = objectMapper.writeValueAsString(command);

		// When & Then
		mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(status().isOk());

		// Verify that orders.placed() was called once with the correct argument
		verify(orders, times(1)).placed(Mockito.any(OrderPlacedCommand.class));
	}
}

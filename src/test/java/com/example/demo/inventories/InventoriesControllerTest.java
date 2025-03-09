package com.example.demo.inventories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisabledInAotMode
@WebMvcTest(InventoriesController.class)
class InventoriesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private Inventories inventories;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldUpdateStockSuccessfully() throws Exception {
		// Given
		UpdateStockCommand command = new UpdateStockCommand("SKU123", 10);
		String requestBody = objectMapper.writeValueAsString(command);
		when(inventories.updateStock(command))
				.thenReturn(new InventoryDto(1L, "SKU123", 10));

		// When & Then
		mockMvc.perform(post("/inventories")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.sku").value("SKU123"))
				.andExpect(jsonPath("$.quantity").value(10));
	}

	@Test
	void shouldAdjustStockSuccessfully() throws Exception {
		// Given
		var command = new AdjustStockCommand("SKU123", 5);
		var requestBody = objectMapper.writeValueAsString(command);
		when(inventories.adjustStock(any(AdjustStockCommand.class)))
				.thenReturn(new InventoryDto(1L, "SKU123", 5));

		// When & Then
		mockMvc.perform(patch("/inventories/adjust")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.sku").value("SKU123"))
				.andExpect(jsonPath("$.quantity").value(5));
	}
}

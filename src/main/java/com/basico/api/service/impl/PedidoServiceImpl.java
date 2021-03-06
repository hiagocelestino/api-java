package com.basico.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.basico.api.model.ItemPedido;
import com.basico.api.model.Pedido;
import com.basico.api.model.Produto;
import com.basico.api.repository.ItemPedidoRepository;
import com.basico.api.repository.PedidoRepository;
import com.basico.api.service.PedidoService;

@Component("PedidoController")
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	ProdutoServiceImpl produtoServiceImpl;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	public Optional<Iterable<Pedido>> obterTodosPedidos(Long clienteId){
		return pedidoRepository.findAllByClienteId(clienteId);
	}
	
	public Optional<Pedido> ObterPedidoPeloId(Long id) {
		return pedidoRepository.findById(id);
	}
	
	public Pedido cadastrarPedido(Long clienteId, Long produtoId, Double desconto, int quantidade) {
		try {
			Optional<Produto> retorno = produtoServiceImpl.obterProdutoPeloId(produtoId);
			if(retorno.isPresent()) {
				
				Produto produto = retorno.get();
				
				Pedido pedido = new Pedido(desconto, clienteId);
				
				ItemPedido item = new ItemPedido(quantidade, produto, pedido);
				
				pedido.adicionarItem(item);	
				
				return pedidoRepository.save(pedido);	
			}
			else {
				throw new IllegalArgumentException("Produto indísponivel.");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Não foi possível realizar o pedido!" + e);
		}
	}
	
	public Pedido editarPedido(Pedido pedido) {
		Optional<Pedido> retorno = ObterPedidoPeloId(pedido.getId());
		if(retorno.isPresent()) {
			return pedidoRepository.save(pedido);
		}
		else {
			throw new IllegalArgumentException("Não foi possível encontrar o pedido.");
		}
	}
	
	public void deletarPedido(Long id) {
		Optional<Pedido> retorno = ObterPedidoPeloId(id);
		if(retorno.isPresent()) {
			pedidoRepository.deleteById(id);
		} 
		else {
			throw new IllegalArgumentException("Pedido não encontrado.");
		}
	}

}

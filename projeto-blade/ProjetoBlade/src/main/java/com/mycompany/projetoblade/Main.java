package com.mycompany.projetoblade;

import com.mycompany.projetoblade.controller.VeiculoController;
import com.mycompany.projetoblade.controller.ClienteController;
import com.mycompany.projetoblade.controller.VendaController;
import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.VeiculoEletrico;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Venda;
import com.mycompany.projetoblade.repository.VeiculoRepository;
import com.mycompany.projetoblade.repository.ClienteRepository;
import com.mycompany.projetoblade.repository.VendaRepository;
import com.mycompany.projetoblade.repository.VeiculoRepositoryImpl;
import com.mycompany.projetoblade.repository.ClienteRepositoryImpl;
import com.mycompany.projetoblade.repository.VendaRepositoryImpl;
import com.mycompany.projetoblade.service.VeiculoService;
import com.mycompany.projetoblade.service.ClienteService;
import com.mycompany.projetoblade.service.VendaService;
import com.mycompany.projetoblade.utils.DataFileReaderUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Classe principal que demonstra o uso do sistema seguindo o padrão MVC.
 * Aplicando SRP - responsável apenas por demonstrar o funcionamento do sistema.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTÃO DE VEÍCULOS - PADRÃO MVC ===\n");
        
        // Inicialização das dependências (Injeção de Dependência)
        VeiculoRepository veiculoRepository = new VeiculoRepositoryImpl();
        ClienteRepository clienteRepository = new ClienteRepositoryImpl();
        VendaRepository vendaRepository = new VendaRepositoryImpl();
        
        VeiculoService veiculoService = new VeiculoService(veiculoRepository);
        ClienteService clienteService = new ClienteService(clienteRepository);
        VendaService vendaService = new VendaService(vendaRepository, veiculoRepository, clienteRepository);
        
        VeiculoController veiculoController = new VeiculoController(veiculoService);
        ClienteController clienteController = new ClienteController(clienteService);
        VendaController vendaController = new VendaController(vendaService, veiculoService, clienteService);
        
        try {
            // 1. Carregar veículos do arquivo de dados
            System.out.println("1. CARREGANDO VEÍCULOS DO ARQUIVO...");
            String caminhoArquivo = "src/main/resources/data/veiculos.txt";
            List<Veiculo> veiculosCarregados = DataFileReaderUtil.carregarVeiculosDoArquivoComTratamentoErro(caminhoArquivo);
            
            if (veiculosCarregados.isEmpty()) {
                System.out.println("Nenhum veículo foi carregado do arquivo. Criando dados de exemplo...");
                criarDadosExemplo(veiculoController, clienteController);
            } else {
                System.out.println("Carregados " + veiculosCarregados.size() + " veículos do arquivo:");
                for (Veiculo veiculo : veiculosCarregados) {
                    veiculoController.adicionarVeiculo(veiculo);
                    System.out.println("  - " + veiculo.getModelo() + " " + veiculo.getMarca() + 
                                     " (" + veiculo.getAno() + ") - R$ " + String.format("%.2f", veiculo.getPreco()));
                }
            }
            
            System.out.println("\n2. LISTANDO TODOS OS VEÍCULOS:");
            listarVeiculos(veiculoController);
            
            System.out.println("\n3. DEMONSTRANDO BUSCAS:");
            demonstrarBuscas(veiculoController);
            
            System.out.println("\n4. DEMONSTRANDO VENDAS:");
            demonstrarVendas(veiculoController, clienteController, vendaController);
            
            System.out.println("\n5. DEMONSTRANDO RELATÓRIOS:");
            demonstrarRelatorios(vendaController);
            
            System.out.println("\n6. DEMONSTRANDO PRINCÍPIOS OCP (Open/Closed):");
            demonstrarOCP(veiculoController);
            
        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
        }
    }
    
    private static void criarDadosExemplo(VeiculoController veiculoController, ClienteController clienteController) {
        // Criar veículos de exemplo
        Veiculo veiculo1 = new Veiculo("Civic", "Honda", 2020, "ABC1234", "1HGBH41JXMN109186");
        veiculo1.setPreco(85000.00);
        veiculo1.setStatus("DISPONIVEL");
        veiculoController.adicionarVeiculo(veiculo1);
        
        Veiculo veiculo2 = new Veiculo("Corolla", "Toyota", 2021, "DEF5678", "1NXBU4EE0MZ123456");
        veiculo2.setPreco(92000.00);
        veiculo2.setStatus("DISPONIVEL");
        veiculoController.adicionarVeiculo(veiculo2);
        
        // Criar veículo elétrico (demonstrando OCP)
        VeiculoEletrico veiculoEletrico = new VeiculoEletrico(
            "Model 3", "Tesla", 2022, "STU5678", "5YJ3E1EA4NF123456",
            500.0, "Li-ion", 8.0
        );
        veiculoEletrico.setPreco(180000.00);
        veiculoEletrico.setStatus("DISPONIVEL");
        veiculoController.adicionarVeiculo(veiculoEletrico);
        
        // Criar clientes de exemplo
        clienteController.criarCliente(
            "João Silva", "joao@email.com", "123456", "12345678901", "Rua A, 123"
        );
        
        clienteController.criarCliente(
            "Maria Santos", "maria@email.com", "654321", "98765432100", "Rua B, 456"
        );
        
        System.out.println("Dados de exemplo criados com sucesso!");
    }
    
    private static void listarVeiculos(VeiculoController veiculoController) {
        List<Veiculo> veiculos = veiculoController.listarVeiculos();
        for (Veiculo veiculo : veiculos) {
            System.out.println("  - " + veiculo);
        }
    }
    
    private static void demonstrarBuscas(VeiculoController veiculoController) {
        System.out.println("  Buscando veículos da Honda:");
        List<Veiculo> veiculosHonda = veiculoController.buscarVeiculosPorMarca("Honda");
        veiculosHonda.forEach(v -> System.out.println("    " + v.getModelo() + " - R$ " + String.format("%.2f", v.getPreco())));
        
        System.out.println("\n  Buscando veículos por faixa de preço (R$ 80.000 - R$ 100.000):");
        List<Veiculo> veiculosFaixaPreco = veiculoController.buscarVeiculosPorFaixaPreco(80000, 100000);
        veiculosFaixaPreco.forEach(v -> System.out.println("    " + v.getModelo() + " " + v.getMarca() + " - R$ " + String.format("%.2f", v.getPreco())));
        
        System.out.println("\n  Buscando veículos disponíveis:");
        List<Veiculo> veiculosDisponiveis = veiculoController.listarVeiculosDisponiveis();
        veiculosDisponiveis.forEach(v -> System.out.println("    " + v.getModelo() + " " + v.getMarca() + " - " + v.getStatus()));
    }
    
    private static void demonstrarVendas(VeiculoController veiculoController, ClienteController clienteController, VendaController vendaController) {
        // Listar clientes
        List<Cliente> clientes = clienteController.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("  Nenhum cliente cadastrado.");
            return;
        }
        
        // Listar veículos disponíveis
        List<Veiculo> veiculosDisponiveis = veiculoController.listarVeiculosDisponiveis();
        if (veiculosDisponiveis.isEmpty()) {
            System.out.println("  Nenhum veículo disponível para venda.");
            return;
        }
        
        // Realizar uma venda
        Cliente cliente = clientes.get(0);
        Veiculo veiculo = veiculosDisponiveis.get(0);
        
        if (cliente == null || veiculo == null) {
            System.out.println("  Erro: Cliente ou veículo não encontrado.");
            return;
        }
        
        System.out.println("  Realizando venda:");
        System.out.println("    Cliente: " + cliente.getUsuario().getNome());
        System.out.println("    Veículo: " + veiculo.getModelo() + " " + veiculo.getMarca());
        System.out.println("    Valor: R$ " + String.format("%.2f", veiculo.getPreco()));
        
        try {
            Venda venda = vendaController.criarVenda(
                cliente.getId(), 
                veiculo.getIdVeiculo(), 
                veiculo.getPreco(), 
                "PIX"
            );
            System.out.println("    Venda realizada com sucesso! ID: " + venda.getIdVenda());
        } catch (Exception e) {
            System.out.println("    Erro ao realizar venda: " + e.getMessage());
        }
    }
    
    private static void demonstrarRelatorios(VendaController vendaController) {
        List<Venda> vendas = vendaController.listarVendas();
        System.out.println("  Total de vendas: " + vendas.size());
        
        if (!vendas.isEmpty()) {
            double totalVendas = vendas.stream().mapToDouble(Venda::getValorFinal).sum();
            System.out.println("  Valor total das vendas: R$ " + String.format("%.2f", totalVendas));
            
            double mediaVendas = vendas.stream().mapToDouble(Venda::getValorFinal).average().orElse(0.0);
            System.out.println("  Valor médio das vendas: R$ " + String.format("%.2f", mediaVendas));
        }
        
        // Relatório do mês atual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<Venda> vendasMes = vendaController.buscarVendasPorPeriodo(inicioMes, fimMes);
        System.out.println("  Vendas do mês atual: " + vendasMes.size());
    }
    
    private static void demonstrarOCP(VeiculoController veiculoController) {
        System.out.println("  Demonstração do princípio OCP (Open/Closed):");
        System.out.println("  - Veículos convencionais e elétricos são tratados de forma polimórfica");
        
        List<Veiculo> veiculos = veiculoController.listarVeiculos();
        for (Veiculo veiculo : veiculos) {
            if (veiculo instanceof VeiculoEletrico eletrico) {
                System.out.println("    Veículo Elétrico: " + eletrico.getModelo() + 
                                 " - Autonomia: " + eletrico.getAutonomiaBateria() + "km" +
                                 " - Bateria: " + eletrico.getTipoBateria() +
                                 " - Tempo de carregamento: " + eletrico.getTempoCarregamento() + "h");
            } else {
                System.out.println("    Veículo Convencional: " + veiculo.getModelo() + " " + veiculo.getMarca());
            }
        }
        
        System.out.println("\n  O sistema pode ser estendido com novos tipos de veículos");
        System.out.println("  sem modificar o código existente (princípio OCP).");
    }
}

## Projeto final de Orientação a Objetos 2 <br>
Este projeto é um gerenciador de eventos, aonde você pode se registrar e participar de eventos, <br>
enquanto os administradores criam e gerenciam eles.

Parte BD: Yan Jardim Leal <br>
Parte Dao: Yan Jardim Leal <br>
Parte Service: Yan Jardim Leal <br>
Parte Interação com a Interface: Rafael Adonis Menon & Gabriel Capri <br>
Parte Design da Interface: Rafael Adonis Menon & Gabriel Capri <br>

## BD:

-- Tabela de usuários

	CREATE TABLE usuario (
    		id SERIAL PRIMARY KEY,
    		nome_completo VARCHAR(255) NOT NULL,
    		email VARCHAR(255) UNIQUE NOT NULL,
    		senha VARCHAR(255) NOT NULL,
    		tipo VARCHAR(50) NOT NULL -- Tipo do usuário: 'participante' ou 'administrador'
	);

-- Tabela administradores

	CREATE TABLE administrador (
    		id INT PRIMARY KEY REFERENCES usuario(id),
    		cargo VARCHAR(100) NOT NULL,
    		data_contratado DATE NOT NULL
	);

-- Tabela participantes

	CREATE TABLE participante (
    		id INT PRIMARY KEY REFERENCES usuario(id),
   		cpf VARCHAR(11) UNIQUE NOT NULL,
    		data_nascimento DATE NOT NULL
	);

-- Tabela eventos

	CREATE TABLE evento (
    		id SERIAL PRIMARY KEY,
    		titulo VARCHAR(255) NOT NULL,
    		descricao TEXT,
    		categoria VARCHAR(100),
    		data DATE NOT NULL,
    		hora TIME NOT NULL,
    		duracao INTERVAL,
    		preco DECIMAL(10, 2),
    		capacidade_maxima INT NOT NULL,
    		local VARCHAR(255),
    		is_link BOOLEAN NOT NULL,
    		status VARCHAR(50) -- Status do evento: 'ativo', 'cancelado', depois decide mais
	);

-- Tabela para organizadores (relação entre administrador e evento)

	CREATE TABLE organizadores (
    		admin_id INT REFERENCES administrador(id),
    		evento_id INT REFERENCES evento(id),
    		PRIMARY KEY (admin_id, evento_id)
	);

-- Tabela para participação de participantes nos eventos

	CREATE TABLE participante_evento (
    		participante_evento_id SERIAL PRIMARY KEY,
    		participante_id INT REFERENCES participante(id),
    		evento_id INT REFERENCES evento(id) '
	);

## DAO:

	BancoDados.java ->
		- conectar()
		- Connection desconectar()
		- Connection Properties carregarPropriedades()
		- void finalizarStatement(Statement statement)
		- void finalizarResultSet(ResultSet resultSet)
	UserManagerDao.java ->
		- void conectarBD(Connection conn)
		- boolean cadastrar(User user)
		- User getUserPorEmail(String email)
	EventoManagerDao.java ->

## Service:

	UserManager.java ->
		- boolean cadastrarUsuario(User user)
		- User encontrarUsuario(User user)
		- User login(String email, String senha)
	User.java ->
		- Variáveis:
			protected String nome;
			protected String senha;
			protected String email; // DEVE SER DO TIPO UNIQUE NA HORA DE CRIAR O DB
			protected UsuarioTipo tipo;
		- User(String nome, String senha, String email) // E após isso usar o setTipo();
		- boolean verificarSenha(String senha)
		- getters() nome, email, senha e tipo.
		- setters() tipo.
	Participante.java extends User ->
		- Variáveis:
			private String cpf;
			private Date dataNascimento;
		- Participante(String nome, String senha, String email, String cpf, Date dataNascimento)
		- getters() cpf e dataNascimento.
	Administrador.java extends User ->
		- Variáveis:
			private String cargo;
			private Date dataContratado;
		- Administrador(String nome, String senha, String email, String cargo, Date dataContratado)
		- getters() cargo e dataContratado.
	EventoManager.java ->
		- boolean participarEvento(Participante participante)
		- boolean confirmarPresenca(Participante participante)
		- boolean sairEvento(Participante participante)
			
		- boolean criarEvento(Administrador admin)
		- boolean editarEvento(Administrador admin, VariavelEvento vEvento)
		- boolean excluirEvento(Administrador admin)
		- boolean imprimirEventoXLS(int eventoId)
	ServiceManager.java ->
		- Variáveis:
			private User user;
		- private void verificarAdmin()
		- private void verificarDefault()
		- public ArrayList<User> getUsers(UsuarioTipo tipo)
		- public void gerarRelatorioXLSAdmin()
		- public void criarEvento(Evento evento)
		- public void editarEvento(Evento evento, VariavelEvento variavel)
		- public void excluirEvento()
		- public void gerarRelatorioXLSParticipante()
		- public void participarEvento(Evento evento)
		- public void sairEvento(Evento evento)
		- public void confimarPresenca(Evento evento)
		
## Interface:
	
	LoginWindow.java extends JFrame ->
	    	- private JPanel contentPane;
		- private JLabel lblEmail;
		- private JTextField txtFieldEmail;
		- private JLabel lblSenha;
		- private JPasswordField passwordFieldSenha;
		- private JButton btnRegistrar;
		- private JButton btnLogin;
        	- private RegisterWindow registerWindow;
		
	RegisterWindow extends JFrame ->	
		- private JPanel contentPane;
		- private JLabel lblNomeCompleto;
		- private JTextField textFieldNomeCompleto;
		- private JLabel lblEmail;
		- private JTextField textFieldEmail;
		- private JLabel lblSenha;
		- private JPasswordField passwordField;
		- private JLabel lblDataNascimento;
		- private JFormattedTextField formattedTextFieldDataNascimento;
		- private JLabel lblCpf;
		- private JFormattedTextField formattedTextFieldCpf;
		- private JLabel lblTipoUsuario;
		- private JPasswordField passwordFieldSenhaMestra;
		- private JLabel lblSenhaMestra;
		- private JButton btnRegistrar;
	
FALTA:
Criar relação DAO e SERVICE das palestras
Verificar CPF
Gerar relatório XLS

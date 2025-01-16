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
 	   		id INT PRIMARY KEY AUTO_INCREMENT,
 	   		nome_completo VARCHAR(255) NOT NULL,
 	   		email VARCHAR(255) UNIQUE NOT NULL,
 	   		senha VARCHAR(255) NOT NULL,
 	   		tipo VARCHAR(50) NOT NULL -- Tipo do usuário: 'participante' ou 	'administrador'
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
    			id INT PRIMARY KEY AUTO_INCREMENT,
    			titulo VARCHAR(255) NOT NULL,
    			descricao TEXT,
    			categoria VARCHAR(100),
	    		data DATE NOT NULL,
	    		hora TIME NOT NULL,
	    		duracao VARCHAR(255) NOT NULL,
	    		preco DECIMAL(10, 2),
	    		capacidade_maxima INT NOT NULL,
	    		local VARCHAR(255),
	    		is_link BOOLEAN NOT NULL,
	    		status VARCHAR(50) -- Status do evento: 'ativo', 'cancelado', etc.
		);
	
-- Tabela para organizadores (relação entre administrador e evento)

		CREATE TABLE administrador_evento (
	    		admin_id INT NOT NULL,
	    		evento_id INT NOT NULL,
	    		PRIMARY KEY (admin_id, evento_id),
	    		CONSTRAINT fk_admin FOREIGN KEY (admin_id) REFERENCES administrador(id),
	    		CONSTRAINT fk_evento FOREIGN KEY (evento_id) REFERENCES evento(id)
		);

-- Tabela para participação de participantes nos eventos

		CREATE TABLE participante_evento (
	    		participante_id INT NOT NULL,
	    		evento_id INT NOT NULL,
	    		confirmou_presenca BOOLEAN NOT NULL,
	    		PRIMARY KEY (participante_id, evento_id),
	    		CONSTRAINT fk_participante FOREIGN KEY (participante_id) 		REFERENCES participante(id),
	    		CONSTRAINT fk_evento_participante FOREIGN KEY (evento_id) 		REFERENCES evento(id)
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

## Entidades:
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
	Evento.java ->
		- Variáveis:
				private Integer id;
				private String titulo;
				private String descricao;
				private Date dataEvento;
				private Time horaEvento;
				private Duration duracaoEvento;
				private boolean isLink;
				private String local;
				private int capacidadeMaxima;
				private EventoCategoria categoria;
				private EventoStatus status;
				private double preco;
		- public Evento(variáveis).
		- Todos os getters.
## Service:
	UserManager.java ->
		- public static void cadastrarUsuario(User user) throws Exception
		- public static void apagarUsuario() throws SQLException, UsuarioNaoLogadoException
		- public static boolean usuariosMaiorQue(int quantidade) throws SQLException
	EventoManager.java ->
		- boolean participarEvento(Participante participante)
		- boolean confirmarPresenca(Participante participante)
		- boolean sairEvento(Participante participante)
			
		- boolean criarEvento(Administrador admin)
		- boolean editarEvento(Administrador admin, VariavelEvento vEvento)
		- boolean excluirEvento(Administrador admin)
		- boolean imprimirEventoXLS(int eventoId)
	LoginManager.java ->
		- Variáveis:
			private User usuarioLogado;
		
		- private LoginManager()
		- public static void login(String email, String senha) throws LoginException 
		- public static void logOff()
		- public static boolean isLogado()
		- public static User getUsuario()
		
		//Importante:
		P: 	Como o service funcionará?
		R: 	Por meio do Login Manager, User Manager e Evento Manager,
			primeiro utilizamos o Login Manager para realizar um login ou um User Manager
			para criar uma conta de usuário. após isso o usuário estará liberado a suas
			respectivas funções de classe.
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

CONCEITOS:
O primeiro usuário a se registrar deverá ser obrigatóriamente um administrador
administradores podem registrar outros administradores
usuários normais que se registram devem se registrarem na tela de login
um administrador pode deletar a conta de um usuário normal

FUNCIONAMENTO:

A primeira coisa que o programa faz é tentar se conectar ao banco de dados, caso ele não consiga o programa irá
apresentar um erro e fechar após 3 tentativas

Caso bem sucedido ele irá abrir a tela de login
Caso o usuário não tenha uma conta ele poderá se registrar

Dando o login você ganha acesso a respectiva conta que lhe foi dado acesso



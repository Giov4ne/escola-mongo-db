Sistema de Gerenciamento Escolar

Este projeto é um sistema de gerenciamento acadêmico desenvolvido em Java com a IDE NetBeans, como trabalho final da disciplina de Banco de Dados.
A aplicação opera em modo console e foi migrada de um banco de dados PostgreSQL para conectar-se a um banco NoSQL MongoDB (hospedado no MongoDB Atlas). O sistema permite realizar o gerenciamento completo de alunos, professores, cursos, e turmas, além de outras operações, como gerar relatórios e cadastrar avaliações.

Pré-requisitos

Para executar o projeto, você precisará ter o seguinte ambiente configurado:  
    -> Apache NetBeans IDE: Versão 12.0 ou superior.  
    -> Java Development Kit (JDK): Versão 11 ou superior.  
    -> Drivers do MongoDB: É necessário adicionar três arquivos .jar para permitir a comunicação com o banco - bson-4.11.1.jar; mongodb-driver-core-4.11.1.jar; e mongodb-driver-sync-4.11.1.jar. Certifique-se de baixar exatamente essas versões para garantir compatibilidade.

Como Executar no NetBeans

1. Abra o Projeto: Com o NetBeans aberto, vá em Arquivo > Abrir Projeto e selecione a pasta do projeto.
2. Adicione os Drivers
    -> Na árvore de projetos à esquerda (aba Projetos), localize a pasta Bibliotecas (ou Libraries).  
    -> Clique com o botão direito sobre ela e selecione Adicionar JAR/Pasta.  
    -> Navegue até a pasta onde você salvou os drivers.  
    -> Selecione os três arquivos (bson, core e sync) de uma vez e clique em Abrir.  
    -> Verifique se eles apareceram listados dentro da pasta Bibliotecas do projeto.  
3. Execute o Projeto:
    -> Encontre o arquivo principal, chamado Principal.java (dentro do pacote default).  
    -> Clique com o botão direito sobre ele e escolha a opção Executar Arquivo (ou pressione Shift + F6).  
    -> O menu principal do sistema aparecerá no console de saída do NetBeans.  

Observação: A conexão com o cluster do MongoDB Atlas já está configurada diretamente na classe ConexaoMongoDB.java. Não é necessário alterar a string de conexão (URI), usuários ou senhas para que o programa funcione, desde que o computador tenha acesso à internet.

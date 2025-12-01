Sistema de Gerenciamento Escolar

Este projeto é um sistema de gerenciamento acadêmico desenvolvido em Java com a IDE NetBeans, como parte de um trabalho da disciplina de Banco de Dados.
A aplicação opera em modo console e se conecta a um banco de dados PostgreSQL (hospedado na Neon) para realizar o gerenciamento completo de alunos, professores, cursos, disciplinas, notas e matrículas.

Pré-requisitos

Para executar o projeto, você precisará ter o seguinte ambiente configurado:
    -> Apache NetBeans IDE: Versão 12.0 ou superior.  
    -> Java Development Kit (JDK): Versão 11 ou superior.  
    -> Driver JDBC do PostgreSQL: O arquivo .jar que permite a comunicação com o banco de dados.  

Como Executar no NetBeans

1. Abra o Projeto: Com o NetBeans aberto, vá em Arquivo > Abrir Projeto e selecione a pasta do projeto.
2. Adicione o Driver JDBC
    -> Na árvore de projetos à esquerda, clique com o botão direito sobre a pasta "Bibliotecas" (ou "Libraries").  
    -> Selecione a opção "Adicionar JAR/Pasta".  
    -> Navegue até o local onde você salvou o arquivo .jar do driver do PostgreSQL e selecione-o.  
3. Execute o Projeto:
    -> Encontre o arquivo principal, chamado Principal.java.  
    -> Clique com o botão direito sobre ele e escolha a opção "Executar Arquivo" (ou pressione Shift + F6).  
    -> O menu principal do sistema aparecerá no console de saída do NetBeans.  

Observação: A conexão com o banco de dados Neon já está configurada diretamente no código-fonte. Não é necessário alterar usuários, senhas ou endereços de conexão para que o programa funcione.

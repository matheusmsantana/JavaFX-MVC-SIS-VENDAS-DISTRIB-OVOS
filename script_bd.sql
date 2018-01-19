CREATE TABLE categorias(
   cdCategoria serial     NOT NULL,
   descricao  varchar(50) NOT NULL,
   CONSTRAINT pk_categorias
      PRIMARY KEY(cdCategoria)
);

CREATE TABLE produtos(
   cdProduto varchar(70)     NOT NULL,
   nome varchar(50) NOT NULL,
   preco float     NOT NULL,
   quantidade int     NOT NULL,
   cdCategoria int     NOT NULL,
   CONSTRAINT pk_produtos
      PRIMARY KEY(cdProduto),
   CONSTRAINT fk_produtos_categorias
      FOREIGN KEY(cdCategoria)
      REFERENCES categorias(cdCategoria)
);

CREATE TABLE clientes(
   cdCliente serial      NOT NULL,
   nome varchar(50) NOT NULL,
   cpf varchar(50) NOT NULL,
   telefone varchar(50) NOT NULL,
   CONSTRAINT pk_clientes
      PRIMARY KEY(cdCliente)
);

CREATE TABLE vendas(
   cdVenda serial NOT NULL,
   data date NOT NULL,
   valor float NOT NULL,
   pago boolean NOT NULL,
   cdCliente int,
   CONSTRAINT pk_vendas
      PRIMARY KEY(cdVenda),
   CONSTRAINT fk_vendas_clientes
      FOREIGN KEY(cdCliente)
      REFERENCES clientes(cdCliente)
);

CREATE TABLE itensdevenda(
   cdItemDeVenda serial NOT NULL,
   quantidade int NOT NULL,
   valor float NOT NULL,
   cdProduto varchar,
   cdVenda int,
   CONSTRAINT pk_itensdevenda
      PRIMARY KEY(cdItemDeVenda),
   CONSTRAINT fk_itensdevenda_produtos
      FOREIGN KEY(cdProduto)
      REFERENCES produtos(cdProduto),
   CONSTRAINT fk_itensdevenda_vendas
      FOREIGN KEY(cdVenda)
      REFERENCES vendas(cdVenda)
);

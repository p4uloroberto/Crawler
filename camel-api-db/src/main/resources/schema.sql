create table transacao (
   id bigint,
   descricao varchar(255) not null,
   tipo_transacao integer not null,
   vlr_transacao DECIMAL(20, 2) not null,
   dt_transacao timestamp not null,
   is_ativo integer not null,
   conta varchar(14) not null,
   primary key (id)
);

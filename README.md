# 🖥️ Sistema de Controle e Gestão de Ativos

Sistema desktop desenvolvido em Java para controle, rastreamento e gestão de ativos corporativos, permitindo o cadastro, consulta, atualização e gerenciamento de equipamentos e patrimônios de forma centralizada.

---

## 📌 Objetivo

Centralizar o controle de ativos de TI e patrimônio corporativo, proporcionando:

- Maior rastreabilidade dos equipamentos
- Organização dos dados patrimoniais
- Controle de responsáveis e localizações
- Redução de controles paralelos em planilhas
- Apoio à tomada de decisão

---

## 🚀 Funcionalidades

### 📦 Gestão de Ativos

- Cadastro de ativos
- Atualização de ativos
- Exclusão de ativos
- Consulta de equipamentos
- Busca por patrimônio

### 🏢 Gestão Organizacional

- Cadastro de empresas
- Cadastro de localidades
- Associação de localidades por empresa

### 📊 Controle Operacional

- Dashboard principal
- Controle de status dos equipamentos
- Controle de patrimônio
- Controle de responsáveis
- Controle de localização

### 📤 Exportação de Dados

- Exportação para Excel
- Integração com Apache POI

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura baseada em MVC (Model-View-Controller).

```text
App
 ↓
Controller
 ↓
DAO
 ↓
SQLite
```

### Camadas

- View
- Controller
- DAO
- Model
- Util

---

## 🗄️ Banco de Dados

Banco de dados local utilizando SQLite.

### Principais Entidades

- Ativos
- Empresas
- Localidades

---

## 📂 Estrutura do Projeto

```text
controle-ativos/
│
├── src/
│
├── database/
│   └── ativos.db
│
├── docs/
│   ├── screenshots/
│   └── arquitetura.md
│
├── releases/
│
├── README.md
├── CHANGELOG.md
└── .gitignore
```

---

## 🛠️ Tecnologias Utilizadas

- Java
- JavaFX
- SQLite
- JDBC
- Apache POI
- Git
- GitHub

---

## 📸 Capturas de Tela

### Tela Principal

docs/screenshots/tela-principal.png

---

## 📈 Roadmap

### Próximas Melhorias

- [ ] Finalizar refatoração MVC
- [ ] Controle de usuários
- [ ] Histórico de movimentações
- [ ] Auditoria de alterações
- [ ] Dashboard gerencial avançado
- [ ] Relatórios automatizados
- [ ] Indicadores por localidade
- [ ] Indicadores por empresa

---

## 🔄 Versionamento

Consulte o arquivo:

```text
CHANGELOG.md
```

para acompanhar as evoluções do sistema.

---

## 👨‍💻 Autor

**René Araújo**

GitHub:
🔗 https://github.com/AraujoR3n3

---

## 📌 Contexto

Projeto desenvolvido com foco em gestão e controle de ativos corporativos, aplicando conceitos de arquitetura MVC, persistência de dados, interface gráfica com JavaFX e versionamento utilizando Git e GitHub.
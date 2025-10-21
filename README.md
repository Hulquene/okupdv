# Projeto Java Desktop (Swing + Maven)

Este é um projeto **Java Swing** construído com **Maven**.  
A aplicação conecta-se a uma base de dados configurada através de **variáveis de ambiente**.

---

## 📝 Notas

- **Requisitos:** Java 17+ e Maven instalados.  
- Se usares **NetBeans**, basta abrir o projeto (ficheiro `pom.xml`) e correr pela IDE.  
- Para distribuir a aplicação como **JAR executável**, podes usar o plugin [maven-shade-plugin](https://maven.apache.org/plugins/maven-shade-plugin/).  


---

## ⚙️ Configuração

Antes de correr a aplicação, precisas definir as seguintes variáveis de ambiente no teu sistema:

| Variável        | Descrição                      | Exemplo                  |
|-----------------|--------------------------------|--------------------------|
| `oku_host`      | Endereço do servidor da BD     | `localhost`              |
| `oku_port`      | Porta da BD                    | `3306`                   |
| `oku_username`  | Utilizador da BD               | `root`                   |
| `oku_password`  | Senha da BD (se necessário)    | `12345`                  |
| `oku_database`  | Nome da base de dados          | `meu_projeto`            |


String dbPassword = ""; // senha vazia por padrão
// String dbPassword = System.getenv("oku_password"); // alternativa com variável de ambiente


### 🔹 Windows (PowerShell)
```powershell
setx oku_host "localhost"
setx oku_port "3306"
setx oku_username "root"
setx oku_password "12345"
setx oku_database "meu_projeto"


🔹 Linux / macOS (Bash/Zsh)
export oku_host=localhost
export oku_port=3306
export oku_username=root
export oku_password=12345
export oku_database=meu_projeto



📁 Estrutura do projeto
src/
 ├── main/java      # Código fonte
 ├── main/resources # Recursos (ícones, configs)
 └── test           # Testes unitários
pom.xml             # Configuração Maven





echo "# okupdv" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/Hulquene/okupdv.git
git push -u origin main



Em desevolvimento

Cliente: concluido
Usuario:concluido
Roles usuario -> profile
Fornecedor:concluido
Produto:concluido
Produto categoria:concluido
Caixa ou pdv
    pontos: codigo do supervisor para cancelar a fatura
            so esta a fazer fatura com consumidor final
Financeiro
Inventario




🔑 Atalhos de Teclado Recomendados para PDV

(alguns são praticamente “padrão de mercado”)

Operações Principais

F1 → Ajuda / Informações rápidas

F2 → Novo cliente / Selecionar cliente

F3 → Procurar produto

F4 → Adicionar item manual

F5 → Aplicar desconto

F6 → Ver subtotal

F7 → Suspender venda

F8 → Retomar venda suspensa

F9 → Cancelar item selecionado

F10 → Cancelar venda

F11 → Mudar utilizador (logout rápido)

F12 → Finalizar venda / Pagamento

Teclas Especiais

ESC → Cancelar operação atual / Fechar janela

ENTER → Confirmar (ex.: adicionar item pesquisado)

TAB → Avançar entre campos

CTRL + P → Imprimir

CTRL + D → Abrir gaveta de dinheiro






📥 Contas a Pagar

São todas as obrigações financeiras da empresa, ou seja, aquilo que tens de pagar.

Exemplo:

Compras de mercadorias feitas a fornecedores (a crédito).

Aluguel, água, luz, internet.

Salários, impostos, taxas.

💡 No sistema: normalmente vêm do módulo de compras → cada compra registada vira uma conta a pagar.

📤 Contas a Receber

São todos os direitos financeiros da empresa, ou seja, aquilo que os clientes te devem.

Exemplo:

Vendas a prazo (quando vendes e o cliente paga depois).

Faturas emitidas e ainda não pagas.

💡 No sistema: normalmente vêm do módulo de vendas/faturação → cada fatura a crédito vira uma conta a receber.

⚖️ Diferença essencial

Contas a pagar = dinheiro que vai sair do caixa.

Contas a receber = dinheiro que vai entrar no caixa.

📊 No teu sistema (exemplo PDV/ERP)

Contas a receber → podes usar a tabela orders ou invoices (faturas FR/FT).

Contas a pagar → podes usar a tabela purchases (compras, entrada de estoque, despesas).







Quando registar movimentos de stock:

Venda (PDV / Order)

Cada item vendido gera um movimento OUT (saída).

Exemplo: -2 unidades do Produto A, razão = "VENDA FT/123".

Compra / Entrada em armazém

Quando registamos uma compra (Purchase), cada item comprado gera um movimento IN (entrada).

Exemplo: +50 unidades do Produto A, razão = "COMPRA PC/456".

Ajuste manual (Inventário físico)

Quando o gestor faz uma contagem física e precisa corrigir o stock.

Exemplo: +3 unidades, razão = "AJUSTE MANUAL inventário 2025".

Devoluções

Cliente devolve → entrada (IN).

Devolução ao fornecedor → saída (OUT).

Transferência entre armazéns

Sai do armazém A (OUT, razão = "TRANSFERÊNCIA para Armazém B")

Entra no armazém B (IN, razão = "TRANSFERÊNCIA de Armazém A").

📌 Ou seja:

Venda = apenas um dos casos que geram movimentos.

A lógica certa é: qualquer entrada ou saída de stock → criar um registo em stock_movements.

Assim, stock atual = soma de todos os movimentos.






com.okutonda.okudpdv
├── app/                  → ponto de arranque / lógica global
├── data/
│   ├── connection/        → conexões, pool, inicialização (substitui jdbc)
│   │   ├── DatabaseProvider.java
│   │   ├── ConnectionConfig.java (futuro)
│   │   └── DatabaseBootProbe.java
│   ├── dao/               → DAOs refatorados que usam DatabaseProvider
│   └── entities/          → modelos (Clients, Products, etc.) futuramente movidos de `models`
├── controllers/
├── ui/
├── views/
├── utilities/
└── Okudpdv.java

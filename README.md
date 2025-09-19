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






Em sistemas de faturação/ERP desktop (como o teu em Swing), a escolha das cores deve seguir três princípios:

Profissionalismo (não pode parecer infantil).

Legibilidade (bom contraste).

Usabilidade (cores consistentes para botões, alertas, fundo, etc.).

🎨 Paleta de Cores Sugerida (estilo moderno/profissional)

Primária (Ação principal): Azul médio → #1976D2

Secundária (Apoio/menus): Cinza escuro → #424242

Sucesso (operações OK): Verde → #388E3C

Aviso (atenção): Laranja → #F57C00

Erro (crítico/cancelar): Vermelho → #D32F2F

Fundo principal: Cinza muito claro → #F5F5F5

Texto padrão: Preto/Cinza escuro → #212121

Texto secundário: Cinza médio → #616161

Realce de campos ativos: Azul claro → #64B5F6

📌 Onde aplicar cada cor no teu sistema
1. JFrame / JDialog / JPanel

Fundo da janela: cinza claro #F5F5F5

Cabeçalho/título (se usares um painel superior): azul primário #1976D2

Bordas: cinza médio #E0E0E0

2. Botões

Ação principal (Finalizar Venda / Gravar) → Azul #1976D2 com texto branco.

Ações secundárias (Novo Cliente, Procurar Produto) → Cinza #424242 com texto branco.

Cancelar/Excluir → Vermelho #D32F2F.

Confirmar (OK, Sucesso) → Verde #388E3C.

3. Tabelas (JTable)

Fundo: branco #FFFFFF.

Cabeçalho: cinza médio #EEEEEE com texto preto.

Linhas alternadas: branco / cinza muito claro #FAFAFA.

Seleção: azul claro #64B5F6 com texto preto.

4. Campos de Texto (JTextField, JComboBox)

Fundo normal: branco.

Fundo quando desabilitado: cinza claro #EEEEEE.

Borda ativa (quando focado): azul claro #64B5F6.

5. Mensagens (JOptionPane ou JDialog customizado)

Info → Azul.

Sucesso → Verde.

Aviso → Laranja.

Erro → Vermelho.


Vou-te dar a paleta completa em:

Hexadecimal (#RRGGBB) → padrão web.

Decimal RGB (new Color(r, g, b)).

Float RGB (new Color(r/255f, g/255f, b/255f) → útil para gráficos).

HSL (caso precises para design/UI consistency).

🎨 Paleta de Cores — Vários Formatos
🔵 Primária (Ação principal) → Azul médio

Hex: #1976D2

RGB: new Color(25, 118, 210)

RGB float: new Color(0.098f, 0.463f, 0.824f)

HSL: 207°, 79%, 46%

⚫ Secundária (Menus) → Cinza escuro

Hex: #424242

RGB: new Color(66, 66, 66)

RGB float: new Color(0.259f, 0.259f, 0.259f)

HSL: 0°, 0%, 26%

✅ Sucesso → Verde

Hex: #388E3C

RGB: new Color(56, 142, 60)

RGB float: new Color(0.220f, 0.557f, 0.235f)

HSL: 123°, 43%, 39%

⚠️ Aviso → Laranja

Hex: #F57C00

RGB: new Color(245, 124, 0)

RGB float: new Color(0.961f, 0.486f, 0.0f)

HSL: 32°, 100%, 48%

❌ Erro → Vermelho

Hex: #D32F2F

RGB: new Color(211, 47, 47)

RGB float: new Color(0.827f, 0.184f, 0.184f)

HSL: 0°, 64%, 51%

🪟 Fundo Principal → Cinza muito claro

Hex: #F5F5F5

RGB: new Color(245, 245, 245)

RGB float: new Color(0.961f, 0.961f, 0.961f)

HSL: 0°, 0%, 96%

📝 Texto padrão → Preto/Cinza escuro

Hex: #212121

RGB: new Color(33, 33, 33)

RGB float: new Color(0.129f, 0.129f, 0.129f)

HSL: 0°, 0%, 13%

📝 Texto secundário → Cinza médio

Hex: #616161

RGB: new Color(97, 97, 97)

RGB float: new Color(0.380f, 0.380f, 0.380f)

HSL: 0°, 0%, 38%

🔵 Realce de campos ativos → Azul claro

Hex: #64B5F6

RGB: new Color(100, 181, 246)

RGB float: new Color(0.392f, 0.710f, 0.965f)

HSL: 207°, 89%, 68%


// Definição centralizada das cores
public class TemaCores {

    public static final Color PRIMARY = new Color(25, 118, 210);   // Azul
    public static final Color SECONDARY = new Color(66, 66, 66);   // Cinza escuro
    public static final Color SUCCESS = new Color(56, 142, 60);    // Verde
    public static final Color WARNING = new Color(245, 124, 0);    // Laranja
    public static final Color ERROR = new Color(211, 47, 47);      // Vermelho
    public static final Color BG_LIGHT = new Color(245, 245, 245); // Cinza claro
    public static final Color TEXT_DARK = new Color(33, 33, 33);   // Texto principal
    public static final Color TEXT_GRAY = new Color(97, 97, 97);   // Texto secundário
    public static final Color HIGHLIGHT = new Color(100, 181, 246);// Realce ativo
}





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
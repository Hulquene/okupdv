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
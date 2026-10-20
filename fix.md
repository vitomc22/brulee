Quero que você faça uma rodada de melhorias de UI/UX, animações, identidade visual e correção de fluxo no app.

Antes de alterar qualquer coisa, analise a implementação atual, identifique as telas, componentes, ViewModels/estados e navegação envolvidos. Reutilize a arquitetura e os componentes existentes sempre que possível. Não crie implementações paralelas desnecessárias.

## 1. Adicionar Date Picker na etapa 5 do orçamento

Na **tela/etapa 5 do fluxo de orçamento**, onde o usuário informa a **Data de Entrega**, substitua ou complemente a entrada atual por um Date Picker adequado para Android.

Requisitos:

- O usuário deve conseguir selecionar a data visualmente.
- Não permitir datas anteriores à data atual.
- A data selecionada deve continuar integrada ao estado atual do orçamento.
- Ao voltar e avançar novamente entre etapas, a data escolhida deve permanecer.
- Se existir rascunho salvo, a data deve ser restaurada corretamente.
- Exibir a data usando o padrão brasileiro, por exemplo:

`20/10/2026`

Use preferencialmente os componentes oficiais do Material 3 utilizados pelo projeto.

---

## 2. Redesenhar a tela "Seus Pedidos"

A tela **"Seus Pedidos"** atualmente está visualmente muito simples, com elementos excessivamente grandes e pouca hierarquia de informação.

Quero uma revisão completa dessa tela.

Objetivos:

- deixar a tela mais moderna;
- melhorar densidade de informação;
- diminuir elementos exageradamente grandes;
- melhorar espaçamentos;
- melhorar tipografia;
- criar uma hierarquia visual clara;
- deixar os cards mais compactos;
- facilitar a identificação rápida do status de cada pedido/orçamento.

Cada item deve apresentar de forma organizada, conforme os dados existentes:

- identificação/número do pedido ou orçamento;
- cliente;
- valor;
- data;
- status;
- ação principal disponível;
- informações secundárias realmente relevantes.

Status diferentes devem ser visualmente distinguíveis sem exagerar nas cores.

Exemplos:

- Rascunho
- Aguardando
- Aprovado
- Em produção
- Concluído
- Cancelado

Não quero cards gigantes ocupando quase toda a tela.

A tela precisa funcionar bem tanto com poucos quanto com muitos pedidos.

Mantenha o visual consistente com o restante do aplicativo e com Material 3.

---

## 3. Corrigir bug: rascunho de orçamento não pode ser editado

Existe atualmente um problema funcional importante:

**um orçamento salvo como rascunho não possui opção para continuar/editá-lo.**

Implemente essa funcionalidade.

Quando um orçamento estiver com status de rascunho, deve existir uma ação como:

**"Continuar orçamento"**

ou

**"Editar rascunho"**

Ao selecionar essa opção:

1. carregar todos os dados já preenchidos;
2. restaurar o estado do orçamento;
3. abrir o fluxo de orçamento na etapa adequada;
4. permitir alterações normalmente;
5. salvar novamente no mesmo rascunho, sem criar outro registro duplicado;
6. permitir finalizar o orçamento posteriormente.

Garanta que campos já preenchidos, produtos, quantidades, cliente, valores, datas e demais dados existentes sejam restaurados corretamente.

Verifique também se atualmente existe alguma lógica que sempre cria um novo orçamento ao entrar no fluxo. Caso exista, adapte para suportar claramente dois modos:

- criação de novo orçamento;
- edição/continuação de rascunho existente.

---

## 4. Melhorar animações entre as etapas do orçamento

O fluxo de criação do orçamento atualmente está muito estático.

Quero deixar a experiência mais fluida e agradável usando animações discretas, rápidas e coerentes.

Adicionar animações principalmente em:

- avanço para próxima etapa;
- retorno para etapa anterior;
- troca de conteúdo;
- aparecimento de cards;
- alteração de valores/resumos;
- loading;
- confirmação de ações;
- conclusão do orçamento.

Não quero animações exageradas ou lentas.

Priorize animações que façam sentido para navegação entre etapas, como:

- slide horizontal;
- fade;
- scale leve;
- AnimatedContent;
- AnimatedVisibility;
- transições apropriadas do Compose.

A direção da animação deve acompanhar a navegação sempre que possível:

- avançar → conteúdo entra pela direita;
- voltar → conteúdo entra pela esquerda.

Evite reiniciar estados ou causar recomposições desnecessárias por causa das animações.

---

## 5. Animar o mascote

O mascote também está muito estático.

Existe uma referência de animação no diretório:

`/home/victor/Documentos/git/brulee/anima`

Analise o conteúdo dessa pasta e reutilize/adapte essas animações no aplicativo.

Quero que o mascote tenha vida durante o fluxo de orçamento.

Ele pode ter animações diferentes dependendo do contexto, por exemplo:

- idle;
- carregando/processando;
- usuário avançando etapa;
- sucesso;
- conclusão do orçamento;
- algum feedback positivo.

Não quero que fique se mexendo de forma excessiva a ponto de distrair.

A animação deve complementar a interface.

Se houver arquivos Lottie, GIF, WebP animado ou assets equivalentes nessa pasta, utilize a solução mais adequada para Jetpack Compose e para o formato encontrado.

---

## 6. Adicionar logo e ícone oficial do aplicativo

Atualmente o aplicativo está praticamente sem identidade visual:

- não existe logo devidamente integrada nas telas;
- o ícone exibido no launcher/atalho do Android não utiliza a identidade do app.

Os arquivos oficiais estão em:

`/home/victor/Documentos/git/brulee/app/src/main/assets/logo`

Analise todos os arquivos disponíveis nessa pasta e utilize as versões apropriadas.

### Logo dentro do app

Adicionar a logo onde fizer sentido visualmente, principalmente em pontos como:

- splash;
- tela inicial;
- login, caso exista;
- headers importantes;
- estados vazios ou telas institucionais onde fizer sentido.

Não espalhe a logo por todas as telas desnecessariamente.

### Launcher Icon

Configure corretamente o ícone Android usando a logo disponível.

Criar/configurar:

- `mipmap-mdpi`
- `mipmap-hdpi`
- `mipmap-xhdpi`
- `mipmap-xxhdpi`
- `mipmap-xxxhdpi`

Se o projeto utiliza Adaptive Icon, configurar corretamente:

- foreground;
- background;
- `mipmap-anydpi-v26`.

O resultado precisa funcionar corretamente tanto em launchers modernos quanto em versões anteriores do Android.

Também revise o ícone da splash screen se o projeto estiver utilizando Android SplashScreen API.

---

## 7. Consistência visual geral

Durante essas alterações, aproveite para corrigir inconsistências visuais evidentes encontradas nessas telas.

Padronize:

- paddings;
- espaçamento;
- tamanhos de cards;
- raio de bordas;
- tipografia;
- botões;
- chips;
- ícones;
- estados selecionados;
- feedback visual.

Não faça redesign completo do aplicativo fora do escopo, mas melhore os componentes diretamente relacionados ao fluxo de orçamento e à tela "Seus Pedidos".

Evite valores hardcoded repetidos. Se já existir um Design System/theme no projeto, reutilize-o.

---

## 8. Não quebrar funcionalidades existentes

Depois das mudanças, valide pelo menos estes fluxos:

1. Criar orçamento novo.
2. Avançar e voltar entre todas as etapas.
3. Selecionar data de entrega.
4. Salvar orçamento como rascunho.
5. Sair do fluxo.
6. Abrir "Seus Pedidos".
7. Identificar o rascunho.
8. Clicar em "Continuar orçamento".
9. Restaurar os dados anteriormente preenchidos.
10. Alterar informações.
11. Salvar novamente.
12. Finalizar o orçamento.
13. Abrir novamente "Seus Pedidos" e verificar o novo status.

Também valide:

- rotação/recomposição quando aplicável;
- retorno pelo botão Back;
- persistência do estado;
- ausência de duplicação de rascunhos;
- listas vazias;
- listas com muitos registros.

---

## 9. Qualidade da implementação

Não implemente apenas o mínimo visual.

Quero que você:

- investigue a arquitetura atual antes de modificar;
- reaproveite componentes existentes;
- evite duplicação;
- mantenha separação entre UI, estado e persistência;
- corrija eventuais problemas de estado encontrados no fluxo;
- mantenha compatibilidade com o padrão atual do projeto;
- não introduza bibliotecas novas sem necessidade.

Se precisar adicionar alguma dependência para animação, primeiro verifique se já existe uma biblioteca adequada no projeto.

---

## 10. Ao finalizar

Ao terminar, entregue um resumo contendo:

- arquivos alterados;
- componentes criados;
- componentes reutilizados;
- alterações no fluxo de rascunho;
- como funciona a edição do rascunho;
- alterações de animação;
- como o mascote foi integrado;
- configuração feita para logo/launcher icon;
- testes executados;
- eventuais limitações ou pontos que ainda precisem de atenção.

Não apenas descreva o que deveria ser feito: **faça as alterações no projeto e valide o fluxo completo.**
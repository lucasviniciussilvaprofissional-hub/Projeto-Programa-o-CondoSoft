# Sistema CondoSoft

## Descrição

O CondoSoft é um sistema para a administração de condomínios residenciais ou comerciais. Ele automatiza processos financeiros críticos, como o rateio de despesas mensais e a geração de boletos, além de gerenciar a convivência social, reservas de áreas comuns e o controle de segurança na portaria.

O sistema foca na transparência, permitindo que moradores acompanhem despesas e façam reservas online. Para a administração, oferece ferramentas de controle de inadimplência e gestão de prestadores de serviço, garantindo que as regras do regimento interno sejam aplicadas de forma automática.

## Requisitos Funcionais

### 1. Cadastro Estrutural e Social
- **REQ01**: Cadastrar unidades com sua respectiva fração ideal de rateio.
- **REQ02**: Cadastrar moradores associando-os às unidades (Agregação).
- **REQ03**: Diferenciar papéis via herança: Proprietário, Inquilino e Dependente.
- **REQ04**: Registrar veículos e pets vinculados a cada unidade específica.

### 2. Gestão Financeira
- **REQ05**: Registrar despesas mensais (Fixas e Extraordinárias) usando herança.
- **REQ06**: Calcular rateio mensal baseado na fração ideal e consumos.
- **REQ07**: Registrar pagamentos de boletos e controlar histórico de inadimplência.

### 3. Áreas Comuns e Reservas
- **REQ08**: Permitir a reserva de espaços (Salão, Churrasqueira) via agenda.
- **REQ09**: Registrar lista de convidados para controle de acesso na portaria.
- **REQ10**: Lançar taxas de limpeza automaticamente após o uso de áreas.

### 4. Comunicação e Portaria
- **REQ11**: Registrar avisos gerais e editais de convocação.
- **REQ12**: Registrar entrada/saída de visitantes e prestadores de serviço.
- **REQ13**: Registrar ocorrências disciplinares e reclamações formais.

### 5. Relatórios
- **REQ14**: Gerar balancete mensal de receitas e despesas em PDF.
- **REQ15**: Exportar lista de inadimplentes em CSV para assessoria jurídica.

### 6. Regras e Restrições
- **REQ16**: **Bloquear a reserva** de áreas comuns para unidades com boletos em atraso.
- **REQ17**: **Não permitir reservas** que excedam a capacidade máxima de pessoas do local.
- **REQ18**: **Impedir o cadastro** de dependentes sem um responsável ativo na unidade.
- **REQ19**: **Bloquear a exclusão** de registros financeiros já conciliados em balancetes.
- **REQ20**: **Garantir** que o valor total rateado cubra 100% das despesas lançadas.
- **REQ21**: **Validar** se o horário da reserva respeita o "período de silêncio" do condomínio.

## Possíveis APIs/Bibliotecas
- **Java Mail API** – Envio de boletos.
- **JasperReports** – Balancetes.
- **iText** – Confirmação de reservas e QR codes para convidados.

- Integrante do grupo:
- Lucas Vinicius Cabral da Silva | lucasviniciussilvaprofissional@gmail.com
- Caio Vinicius Ferreira Cabral | mentornap@gmail.com
- Alan Sergio |
- Guilherme Oiveira |

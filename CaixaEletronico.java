import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class CaixaEletronico extends JFrame {

    // Saldo inicial da conta, conforme a regra RN01
    private double saldo = 1000.00;

    private JLabel lblSaldo;
    private JTextField txtValor;
    private JTextArea txtExtrato;

    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public CaixaEletronico() {
        setTitle("Banco ETEC - Caixa Eletrônico");
        setSize(570, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("BANCO ETEC - CAIXA ELETRÔNICO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(65, 20, 440, 30);
        add(lblTitulo);

        JLabel lblTextoSaldo = new JLabel("SALDO DISPONÍVEL:");
        lblTextoSaldo.setFont(new Font("Arial", Font.BOLD, 15));
        lblTextoSaldo.setBounds(35, 75, 200, 25);
        add(lblTextoSaldo);

        lblSaldo = new JLabel(moeda.format(saldo));
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 24));
        lblSaldo.setForeground(new Color(20, 110, 70));
        lblSaldo.setBounds(35, 105, 300, 35);
        add(lblSaldo);

        JLabel lblValor = new JLabel("Valor da operação:");
        lblValor.setBounds(35, 160, 180, 25);
        add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(35, 188, 480, 32);
        add(txtValor);

        JButton btnDepositar = new JButton("DEPOSITAR");
        btnDepositar.setBounds(35, 240, 145, 38);
        add(btnDepositar);

        JButton btnSacar = new JButton("SACAR");
        btnSacar.setBounds(195, 240, 145, 38);
        add(btnSacar);

        JButton btnExtrato = new JButton("EXTRATO");
        btnExtrato.setBounds(355, 240, 160, 38);
        add(btnExtrato);

        JLabel lblHistorico = new JLabel("HISTÓRICO DE OPERAÇÕES");
        lblHistorico.setFont(new Font("Arial", Font.BOLD, 14));
        lblHistorico.setBounds(35, 300, 300, 25);
        add(lblHistorico);

        txtExtrato = new JTextArea();
        txtExtrato.setEditable(false);
        txtExtrato.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtExtrato.setText("Bem-vindo ao Banco ETEC!\nSaldo inicial: "
                + moeda.format(saldo) + "\n");
        JScrollPane rolagem = new JScrollPane(txtExtrato);
        rolagem.setBounds(35, 330, 480, 145);
        add(rolagem);

        btnDepositar.addActionListener(e -> depositar());
        btnSacar.addActionListener(e -> sacar());
        btnExtrato.addActionListener(e -> mostrarExtrato());
    }

    private void atualizarSaldo() {
        lblSaldo.setText(moeda.format(saldo));
    }

    private double lerValor() {
        String texto = txtValor.getText().trim().replace(",", ".");
        return Double.parseDouble(texto);
    }

    private void depositar() {
        try {
            double valor = lerValor();

            if (valor <= 0) {
                JOptionPane.showMessageDialog(this,
                        "O valor deve ser maior que zero.");
            } else {
                saldo += valor;
                atualizarSaldo();

                txtExtrato.append("\nDepósito: " + moeda.format(valor)
                        + " | Saldo: " + moeda.format(saldo));
                JOptionPane.showMessageDialog(this, "Depósito realizado!");
                txtValor.setText("");
            }
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this,
                    "Digite um valor numérico válido.");
        }
    }

    private void sacar() {
        try {
            double valorDigitado = lerValor();

            // Verificações na ordem solicitada no slide
            if (valorDigitado <= 0) {
                JOptionPane.showMessageDialog(this,
                        "O valor deve ser maior que zero.");
            } else if (valorDigitado > saldo) {
                JOptionPane.showMessageDialog(this,
                        "Saldo insuficiente.");
            } else if (valorDigitado > 1000) {
                JOptionPane.showMessageDialog(this,
                        "O saque máximo por operação é R$ 1.000,00.");
            } else if (valorDigitado % 10 != 0) {
                JOptionPane.showMessageDialog(this,
                        "O saque deve ser múltiplo de 10.");
            } else {
                int valor = (int) valorDigitado;

                int notas100 = valor / 100;
                valor = valor % 100;

                int notas50 = valor / 50;
                valor = valor % 50;

                int notas10 = valor / 10;

                saldo -= valorDigitado;
                atualizarSaldo();

                String notas = "Notas entregues:\n"
                        + notas100 + " nota(s) de R$ 100\n"
                        + notas50 + " nota(s) de R$ 50\n"
                        + notas10 + " nota(s) de R$ 10";

                txtExtrato.append("\n\nSaque: " + moeda.format(valorDigitado)
                        + "\n" + notas
                        + "\nSaldo: " + moeda.format(saldo));

                JOptionPane.showMessageDialog(this, notas);
                txtValor.setText("");
            }
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this,
                    "Digite um valor numérico válido.");
        }
    }

    private void mostrarExtrato() {
        JOptionPane.showMessageDialog(this,
                new JScrollPane(new JTextArea(txtExtrato.getText(), 12, 35)),
                "Extrato", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CaixaEletronico tela = new CaixaEletronico();
            tela.setVisible(true);
        });
    }
}

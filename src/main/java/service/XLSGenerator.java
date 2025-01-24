package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import entities.Evento;

public class XLSGenerator {

    // Formatters para datas e horas
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // ==========================|| RELATÓRIO ADMIN ||========================== //
    public static void gerarRelatorioAdmin(Evento evento, String caminhoArquivo) throws IOException {
        try (Workbook workbook = new HSSFWorkbook()) {
            // Sheet 1: Informações do Evento
            Sheet sheetEvento = workbook.createSheet("Informações do Evento");
            preencherDadosEvento(sheetEvento, workbook, evento);

            // Sheet 2: Organizadores
            Sheet sheetOrganizadores = workbook.createSheet("Organizadores");
            preencherOrganizadores(sheetOrganizadores, evento);

            // Sheet 3: Participantes
            Sheet sheetParticipantes = workbook.createSheet("Participantes");
            preencherParticipantes(sheetParticipantes, evento);

            // Salva o arquivo
            try (FileOutputStream outputStream = new FileOutputStream(caminhoArquivo)) {
                workbook.write(outputStream);
            }
        }
    }

    // ==========================|| RELATÓRIO PARTICIPANTE ||========================== //
    public static void gerarRelatorioParticipante(Evento evento, String caminhoArquivo) throws IOException {
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Meu Evento");

            // Informações do Evento
            preencherDadosEvento(sheet, workbook, evento);

            // Organizadores
            int rowNum = sheet.getLastRowNum() + 2;
            Row headerOrg = sheet.createRow(rowNum++);
            headerOrg.createCell(0).setCellValue("Organizadores do Evento:");
            preencherOrganizadores(sheet, rowNum, evento);

            // Presença do Participante
            rowNum = sheet.getLastRowNum() + 2;
            Row headerPresenca = sheet.createRow(rowNum++);
            headerPresenca.createCell(0).setCellValue("Sua Presença:");
            Row presencaRow = sheet.createRow(rowNum);
            presencaRow.createCell(0).setCellValue("Confirmada?");
            presencaRow.createCell(1).setCellValue(evento.getParticipantes().get(evento.getId()).isPresencaConfirmada());

            // Salva o arquivo
            try (FileOutputStream outputStream = new FileOutputStream(caminhoArquivo)) {
                workbook.write(outputStream);
            }
        }
    }

    // ==========================|| MÉTODOS AUXILIARES ||========================== //
    private static void preencherDadosEvento(Sheet sheet, Workbook workbook, Evento evento) {
        String[] headers = {
            "Título", "Descrição", "Data", "Hora", "Duração", 
            "Local", "Categoria", "Status", "Preço", "Capacidade"
        };

        // Estilo para cabeçalho
        CellStyle headerStyle = criarEstiloCabecalho(workbook);

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dados
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(evento.getTitulo());
        dataRow.createCell(1).setCellValue(evento.getDescricao());
        dataRow.createCell(2).setCellValue(evento.getDataEvento().toLocalDate().format(DATE_FORMATTER));
        dataRow.createCell(3).setCellValue(evento.getHoraEvento().toLocalTime().format(TIME_FORMATTER));
        dataRow.createCell(4).setCellValue(formatarDuracao(evento.getDuracaoEvento()));
        dataRow.createCell(5).setCellValue(evento.getLocal());
        dataRow.createCell(6).setCellValue(evento.getCategoria().toString());
        dataRow.createCell(7).setCellValue(evento.getStatus().toString());
        dataRow.createCell(8).setCellValue(evento.getPreco());
        dataRow.createCell(9).setCellValue(evento.getCapacidadeMaxima());
    }

    private static void preencherOrganizadores(Sheet sheet, Evento evento) {
        String[] headers = {"ID", "Nome", "Email", "Cargo"};
        preencherDados(sheet, headers, evento.getOrganizadores().values().stream()
            .map(org -> new String[]{
                String.valueOf(org.getId()),
                org.getNome(),
                org.getEmail(),
                org.getCargo()
            }).toArray(String[][]::new));
    }

    private static void preencherParticipantes(Sheet sheet, Evento evento) {
        String[] headers = {"ID", "Nome", "Email", "CPF", "Presença Confirmada"};
        preencherDados(sheet, headers, evento.getParticipantes().values().stream()
            .map(part -> new String[]{
                String.valueOf(part.getId()),
                part.getNome(),
                part.getEmail(),
                part.getCpf(),
                part.isPresencaConfirmada() ? "Sim" : "Não"
            }).toArray(String[][]::new));
    }

    private static void preencherDados(Sheet sheet, String[] headers, String[][] dados) {
        // Implementação genérica para preencher qualquer lista
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (String[] linha : dados) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < linha.length; i++) {
                row.createCell(i).setCellValue(linha[i]);
            }
        }
    }

    private static CellStyle criarEstiloCabecalho(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private static String formatarDuracao(Duration duracao) {
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();
        return String.format("%d horas e %d minutos", horas, minutos);
    }
}
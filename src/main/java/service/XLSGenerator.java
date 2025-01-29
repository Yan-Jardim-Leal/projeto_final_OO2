package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import entities.*;
import dao.EventoManagerDao;

public class XLSGenerator {

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
            preencherOrganizadores(sheetOrganizadores, workbook, evento);
            
            // Sheet 3: Participantes
            Sheet sheetParticipantes = workbook.createSheet("Participantes");
            preencherParticipantesAdmin(sheetParticipantes, workbook, evento);

            salvarArquivo(workbook, caminhoArquivo);
        }
    }

    // ==========================|| RELATÓRIO PARTICIPANTE ||========================== //
    public static void gerarRelatorioParticipante(Evento evento, int idParticipante, String caminhoArquivo) 
            throws IOException, SQLException {
        
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Meu Evento");
            
            // Informações do Evento
            preencherDadosEvento(sheet, workbook, evento);
            
            // Organizadores
            adicionarSecaoOrganizadores(sheet, workbook, evento);
            
            // Presença do Participante
            adicionarSecaoPresenca(sheet, evento, idParticipante);

            salvarArquivo(workbook, caminhoArquivo);
        }
    }

    // ==========================|| MÉTODOS AUXILIARES ||========================== //
    private static void preencherDadosEvento(Sheet sheet, Workbook workbook, Evento evento) {
        String[] headers = {"Título", "Descrição", "Data", "Hora", "Duração", "Local", "Categoria", "Status", "Preço", "Capacidade"};
        CellStyle headerStyle = criarEstiloCabecalho(workbook);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

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

        autoAjustarColunas(sheet, headers.length);
    }

    private static void preencherOrganizadores(Sheet sheet, Workbook workbook, Evento evento) {
        String[] headers = {"ID", "Nome", "Email", "Cargo"};
        CellStyle headerStyle = criarEstiloCabecalho(workbook);
        
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        HashMap<Integer, Administrador> organizadores = evento.getOrganizadores();
        int rowNum = 1;
        for (Administrador org : organizadores.values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(org.getId());
            row.createCell(1).setCellValue(org.getNome());
            row.createCell(2).setCellValue(org.getEmail());
            row.createCell(3).setCellValue(org.getCargo());
        }

        autoAjustarColunas(sheet, headers.length);
    }

    private static void preencherParticipantesAdmin(Sheet sheet, Workbook workbook, Evento evento) {
        String[] headers = {"ID", "Nome", "Email", "CPF", "Presença Confirmada"};
        CellStyle headerStyle = criarEstiloCabecalho(workbook);
        
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        HashMap<Integer, Participante> participantes = evento.getParticipantes();
        int rowNum = 1;
        for (Participante part : participantes.values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(part.getId());
            row.createCell(1).setCellValue(part.getNome());
            row.createCell(2).setCellValue(part.getEmail());
            row.createCell(3).setCellValue(part.getCpf());
            
            try {
                boolean presenca = EventoManagerDao.getEventoConfirmado(part.getId(), evento.getId());
                row.createCell(4).setCellValue(presenca ? "Sim" : "Não");
            } catch (SQLException e) {
                row.createCell(4).setCellValue("Erro ao verificar");
            }
        }

        autoAjustarColunas(sheet, headers.length);
        System.out.println("Sessão dos participantes adicionada!");
    }

    private static void adicionarSecaoOrganizadores(Sheet sheet, Workbook workbook, Evento evento) {
        int startRow = sheet.getLastRowNum() + 2;
        Row tituloRow = sheet.createRow(startRow);
        tituloRow.createCell(0).setCellValue("Organizadores do Evento:");
        
        String[] headers = {"ID", "Nome", "Email", "Cargo"};
        CellStyle headerStyle = criarEstiloCabecalho(workbook);
        
        Row headerRow = sheet.createRow(startRow + 1);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        HashMap<Integer, Administrador> organizadores = evento.getOrganizadores();
        int rowNum = startRow + 2;
        for (Administrador org : organizadores.values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(org.getId());
            row.createCell(1).setCellValue(org.getNome());
            row.createCell(2).setCellValue(org.getEmail());
            row.createCell(3).setCellValue(org.getCargo());
        }

        autoAjustarColunas(sheet, headers.length);
        System.out.println("Sessão dos organizadores adicionada!");
    }

    private static void adicionarSecaoPresenca(Sheet sheet, Evento evento, int idParticipante) throws SQLException {
        int startRow = sheet.getLastRowNum() + 2;
        Row tituloRow = sheet.createRow(startRow);
        tituloRow.createCell(0).setCellValue("Sua Presença:");
        
        boolean presenca = EventoManagerDao.getEventoConfirmado(idParticipante, evento.getId());
        Row presencaRow = sheet.createRow(startRow + 1);
        presencaRow.createCell(0).setCellValue("Confirmada?");
        presencaRow.createCell(1).setCellValue(presenca ? "Sim" : "Não");
        
        System.out.println("Sessão da presença adicionada!");
    }

    private static CellStyle criarEstiloCabecalho(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private static void autoAjustarColunas(Sheet sheet, int numColumns) {
        for (int i = 0; i < numColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static String formatarDuracao(Duration duracao) {
        if (duracao == null) return "0 horas";
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();
        return String.format("%d horas e %d minutos", horas, minutos);
    }

    private static void salvarArquivo(Workbook workbook, String caminho) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(caminho)) {
            workbook.write(outputStream);
        }
    }
}
package ru.home.service;

import org.springframework.stereotype.Service;
import ru.home.util.RenameUtil;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
public class FileService {


	public static final String COMMA_DELIMITER = ";";
	public static final String COLUMN_DELIMITER = "/";

	public HashMap<String, String> readCsv()  {
		HashMap<String, String> records = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/tables.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITER);
				if (records.get(values[0]+COMMA_DELIMITER+values[1]) == null)  {
					records.put(values[0]+COMMA_DELIMITER+values[1], values[2]+COMMA_DELIMITER+values[3]);
				} else {
					records.put(values[0]+COMMA_DELIMITER+values[1], records.get(values[0]+COMMA_DELIMITER+values[1])+COLUMN_DELIMITER+values[2]+COMMA_DELIMITER+values[3]);
				}

			}
		} catch (IOException e) {
			System.out.println("Не удалось прочитать файл: " + e);
		}
		return records;
	}

	public List<String> generateSql(HashMap<String, String> records) {
		List<String> result = new ArrayList<>();

		for (Map.Entry<String, String> record : records.entrySet()) {
			StringBuilder sql = new StringBuilder();
			String schema = RenameUtil.rename(record.getKey().split(COMMA_DELIMITER)[0]);
			String table = RenameUtil.rename(record.getKey().split(COMMA_DELIMITER)[1]);
			sql.append(String.format("CREATE TABLE %s.%s (", schema, table));

			String[] columns = record.getValue().split(COLUMN_DELIMITER);
			for (String column : columns) {
				String columnName = RenameUtil.rename(column.split(COMMA_DELIMITER)[0]);
				String columnType = RenameUtil.changeType(RenameUtil.rename(column.split(COMMA_DELIMITER)[1]));
				sql.append(columnName).append(" ").append(columnType).append(", ");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(");\n");
			result.add(sql.toString());
		}
		return result;
	}


	public void createFile(List<String> records) throws IOException {
		Date dateTime = Date.from(Instant.now());
		String fileName = String.format("sql_%s.txt", dateTime.toString().replaceAll("[ :]", ""));

		FileOutputStream fileOut = new FileOutputStream(fileName, true);

		try {

			for (String record : records) {
				fileOut.write(record.getBytes());
			}

			fileOut.close();

			System.out.println("Данные записаны в файл");
		} catch (RuntimeException e) {
			System.out.println("Произошла какая-то проблема при записи данных в файл");
			fileOut.close();
			Path fileDeletePath = Paths.get(fileName);
			Files.delete(fileDeletePath);
			System.out.println("Файл был удалён");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

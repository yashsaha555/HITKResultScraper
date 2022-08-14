package com.sourav.resulttracker.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sourav.resulttracker.utils.TemplateConstants;

@Service
public class WebScrapperService implements TemplateConstants {

	@Autowired
	private ChromeDriver driver;
	@Value("${sem.result.url}")
	private String url;
	@Value("${sem.number}")
	private String semNo;
	@Value("${roll.no.field}")
	private String rollField;
	@Value("${show.result.button}")
	private String showResultBtn;
	@Value("${semester.drop.down}")
	private String semesterDropDown;
	@Value("${file.creation.path}")
	private String path;
	@Value("${name.element.id}")
	private String nameElement;
	@Value("${roll.element.id}")
	private String rollElement;
	@Value("${gpa.element.id}")
	private String dgpaElement;
	@Value("${name.slice.length}")
	private int nameSlice;
	@Value("${roll.slice.length}")
	private int rollSlice;
	@Value("${gpa.slice.length}")
	private int gpaSlice;
	@Value("${start.roll.no}")
	private long start;
	@Value("${end.roll.no}")
	private long end;

	@PostConstruct
	public void scrape() throws IOException {
		Scanner sc = new Scanner(System.in);
		FileWriter fileWriter = new FileWriter(path);
		HashMap<String, Double> map = new HashMap<>();
		for (long rollNum = start; rollNum <= end; rollNum++) {
			try {
				driver.get(url);
				driver.findElementByName(semesterDropDown).sendKeys(String.valueOf(semNo));
				driver.findElementByName(rollField).sendKeys(String.valueOf(rollNum));
				driver.findElementById(showResultBtn).click();
				String name = driver.findElementById(nameElement).getText().substring(nameSlice);
				String rollNo = driver.findElementById(rollElement).getText().substring(rollSlice);
				double gpa = Double.parseDouble(driver.findElementById(dgpaElement).getText().substring(gpaSlice));
				map.put(rollNo + "  " + name, gpa);
			} catch (Exception e) {
				continue;
			}
		}
		HashMap<String, Double> sortedMapDesc = sortBasedOnRank(map);
		fileWriter.write(header);
		for (Map.Entry<String, Double> entry : sortedMapDesc.entrySet()) {
			// It will print this in console
			System.out.printf("%-45s%s", entry.getKey(), entry.getValue() + "\n");
			// This will write the details in the text file
			fileWriter.write(String.format("%-45s%s", entry.getKey(), entry.getValue() + "\n"));
		}
		fileWriter.write("\n" + footer + "\n" + credits + "\n" + generationTime);
		// It will flush all the values in text file and close the stream
		fileWriter.close();
		// It will clear the HashMap
		map.clear();
	}

	// This method will sort the GPAs in descending order
	private static HashMap<String, Double> sortBasedOnRank(HashMap<String, Double> hm) {
		return hm.entrySet().stream().sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

	}

}

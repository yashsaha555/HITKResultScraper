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

@Service
public class WebScrapperService {

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

	@PostConstruct
	public void scrape() throws IOException {
		Scanner sc = new Scanner(System.in);
		// Specify the path of the location where you want the file to be created
		FileWriter fileWriter = new FileWriter(path);
		HashMap<String, Double> map = new HashMap<>();
//		System.out.println("Enter the starting autonomy roll no. followed by ending roll no.");
//		long start = sc.nextLong();
//		long end = sc.nextLong();
		long start = 12618013001l;
		long end = 12618013010l;
		for (long rollNum = start; rollNum <= end; rollNum++) {

			try {
				driver.get(url);
				driver.findElementByName(semesterDropDown).sendKeys(String.valueOf(semNo));
				driver.findElementByName(rollField).sendKeys(String.valueOf(rollNum));
				driver.findElementById(showResultBtn).click();
				String name = driver.findElementById(nameElement).getText().substring(nameSlice);
				String rollNo = driver.findElementById(rollElement).getText().substring(rollSlice);
				double gpa = Double.parseDouble(driver.findElementById(dgpaElement).getText().substring(gpaSlice));
				String rollAndName = rollNo + "  " + name;
				map.put(rollAndName, gpa);

			} catch (Exception e) {
				continue;
			}

		}
		HashMap<String, Double> sortedMapDesc = sortBasedOnRank(map);
		for (Map.Entry<String, Double> entry : sortedMapDesc.entrySet()) {
			// It will print this in console
			System.out.printf("%s\t\t\t%s\n", entry.getKey(), entry.getValue());
			fileWriter.write(entry.getKey() + "\t\t\t" + entry.getValue() + "\n");
		}
		fileWriter.close();
		map.clear();
	}

	private static HashMap<String, Double> sortBasedOnRank(HashMap<String, Double> hm) {
		return hm.entrySet().stream().sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

	}

}

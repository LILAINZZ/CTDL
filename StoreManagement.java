import java.io.*;
import java.util.*;

public class StoreManagement {
    private ArrayList<Staff> staffs;
    private ArrayList<String> workingTime;
    private ArrayList<Invoice> invoices;
    private ArrayList<InvoiceDetails> invoiceDetails;
    private ArrayList<Drink> drinks;

    public StoreManagement(String staffPath, String workingTimePath, String invoicesPath, String detailsPath,
            String drinksPath) {
        this.staffs = loadStaffs(staffPath);
        this.workingTime = loadFile(workingTimePath);
        this.invoices = loadInvoices(invoicesPath);
        this.invoiceDetails = loadInvoicesDetails(detailsPath);
        this.drinks = loadDrinks(drinksPath);
    }

    public ArrayList<Staff> getStaffs() {
        return this.staffs;
    }

    public void setStaffs(ArrayList<Staff> staffs) {
        this.staffs = staffs;
    }

    public ArrayList<Drink> loadDrinks(String filePath) {
        ArrayList<Drink> drinksResult = new ArrayList<Drink>();
        ArrayList<String> drinks = loadFile(filePath);

        for (String drink : drinks) {
            String[] information = drink.split(",");
            drinksResult.add(new Drink(information[0], Integer.parseInt(information[1])));
        }

        return drinksResult;
    }

    public ArrayList<Invoice> loadInvoices(String filePath) {
        ArrayList<Invoice> invoiceResult = new ArrayList<Invoice>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new Invoice(information[0], information[1], information[2]));
        }

        return invoiceResult;
    }

    public ArrayList<InvoiceDetails> loadInvoicesDetails(String filePath) {
        ArrayList<InvoiceDetails> invoiceResult = new ArrayList<InvoiceDetails>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new InvoiceDetails(information[0], information[1], Integer.parseInt(information[2])));
        }

        return invoiceResult;
    }

    // requirement 1
    public ArrayList<Staff> loadStaffs(String filePath) {
        // code here and modify the return value
        ArrayList<Staff> staffResult = new ArrayList<>();
        ArrayList<String> staff = loadFile(filePath);

        /*
         * for (String data: staff) {
         * String[] dataArray = data.split(",");
         * int len = dataArray.length;
         * if (len == 3) {
         * staffResult.add(new SeasonalStaff(dataArray[0],
         * dataArray[1],
         * Integer.parseInt(dataArray[2])));
         * }
         * else if (len == 4) {
         * staffResult.add(new FullTimeStaff(dataArray[0],
         * dataArray[1], Integer.parseInt(dataArray[2]),
         * Double.parseDouble(dataArray[3])));
         * }
         * else if (len == 5) {
         * staffResult.add(new Manager(dataArray[0],
         * dataArray[1], Integer.parseInt(dataArray[2]),
         * Double.parseDouble(dataArray[3]),
         * Integer.parseInt(dataArray[4])));
         * }
         * }
         */
        for (String data : staff) {
            String[] dataArray = data.split(",");
            String[] part = dataArray[0].split("(?<=\\D)(?=\\d)");
            if (part[0].equals("TV")) {
                staffResult.add(new SeasonalStaff(dataArray[0],
                        dataArray[1],
                        Integer.parseInt(dataArray[2])));
            } else if (part[0].equals("CT")) {
                staffResult.add(new FullTimeStaff(dataArray[0],
                        dataArray[1], Integer.parseInt(dataArray[2]),
                        Double.parseDouble(dataArray[3])));
            } else if (part[0].equals("QL")) {
                staffResult.add(new Manager(dataArray[0],
                        dataArray[1], Integer.parseInt(dataArray[2]),
                        Double.parseDouble(dataArray[3]),
                        Integer.parseInt(dataArray[4])));
            }
        }
        return staffResult;
    }

    // requirement 2
    public ArrayList<SeasonalStaff> getTopFiveSeasonalStaffsHighSalary() {
        // code here and modify the return value
        HashMap<String, Integer> workTime = new HashMap<>();
        for (int i = 0; i < this.workingTime.size(); i++) {
            String[] data = this.workingTime.get(i).split(",");
            workTime.put(data[0], Integer.parseInt(data[1]));
        }

        ArrayList<SeasonalStaff> seasonalStaffList = new ArrayList<SeasonalStaff>();
        for (int i = 0; i < staffs.size(); i++) {
            String[] sStaffArray = staffs.get(i).toString().split("_");
            if (sStaffArray.length == 3) {
                seasonalStaffList.add(new SeasonalStaff(staffs.get(i).getsID(),
                        staffs.get(i).getsName(), Integer.parseInt(sStaffArray[2])));
            }
        }

        for (int i = 0; i < seasonalStaffList.size() - 1; i++) {
            for (int j = i + 1; j < seasonalStaffList.size(); j++) {
                if (seasonalStaffList.get(i)
                        .paySalary(workTime.get(seasonalStaffList.get(i).getsID())) < seasonalStaffList.get(j)
                                .paySalary(workTime.get(seasonalStaffList.get(j).getsID()))) {
                    SeasonalStaff temp = new SeasonalStaff(seasonalStaffList.get(i).getsID(),
                            seasonalStaffList.get(i).getsName(), seasonalStaffList.get(i).getHourlyWage());

                    seasonalStaffList.get(i).setsID(seasonalStaffList.get(j).getsID());
                    seasonalStaffList.get(i).setsName(seasonalStaffList.get(j).getsName());
                    seasonalStaffList.get(i).setHourlyWage(seasonalStaffList.get(j).getHourlyWage());

                    seasonalStaffList.get(j).setsID(temp.getsID());
                    seasonalStaffList.get(j).setsName(temp.getsName());
                    seasonalStaffList.get(j).setHourlyWage(temp.getHourlyWage());
                }
            }
        }
        ArrayList<SeasonalStaff> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            result.add(seasonalStaffList.get(i));
        }
        return result;
    }

    // requirement 3
    public ArrayList<FullTimeStaff> getFullTimeStaffsHaveSalaryGreaterThan(int lowerBound) {
        // code here and modify the return value
        ArrayList<FullTimeStaff> fullTimeStaffList = new ArrayList<>();
        for (int i = 0; i < this.staffs.size(); i++) {
            String[] data = this.staffs.get(i).toString().split("_");
            if (data.length == 4) {
                fullTimeStaffList.add(new FullTimeStaff(this.staffs.get(i).getsID(), this.staffs.get(i).getsName(),
                        Integer.parseInt(data[3]), Double.parseDouble(data[2])));
            } else if (data.length == 5) {
                fullTimeStaffList.add(new Manager(this.staffs.get(i).getsID(), this.staffs.get(i).getsName(),
                        Integer.parseInt(data[3]), Double.parseDouble(data[2]), Integer.parseInt(data[4])));
            }
        }

        HashMap<String, Integer> workTime = new HashMap<>();
        for (int i = 0; i < this.workingTime.size(); i++) {
            String[] data = this.workingTime.get(i).split(",");
            workTime.put(data[0], Integer.parseInt(data[1]));
        }

        ArrayList<FullTimeStaff> result = new ArrayList<>();
        for (int i = 0; i < fullTimeStaffList.size(); i++) {
            if (fullTimeStaffList.get(i).paySalary(workTime.get(fullTimeStaffList.get(i).getsID())) > lowerBound) {
                result.add(fullTimeStaffList.get(i));
            }
        }
        return result;
    }

    // requirement 4
    public double totalInQuarter(int quarter) {
        double total = 0;
        // code here
        return total;
    }

    // requirement 5
    public Staff getStaffHighestBillInMonth(int month) {
        Staff maxStaff = null;
        // code here
        return maxStaff;
    }

    // load file as list
    public static ArrayList<String> loadFile(String filePath) {
        String data = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader fReader = new BufferedReader(reader);

            while ((data = fReader.readLine()) != null) {
                list.add(data);
            }

            fReader.close();
            reader.close();

        } catch (Exception e) {
            System.out.println("Cannot load file");
        }
        return list;
    }

    public void displayStaffs() {
        for (Staff staff : this.staffs) {
            System.out.println(staff);
        }
    }

    public <E> boolean writeFile(String path, ArrayList<E> list) {
        try {
            FileWriter writer = new FileWriter(path);
            for (E tmp : list) {
                writer.write(tmp.toString());
                writer.write("\r\n");
            }

            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }

    public <E> boolean writeFile(String path, E object) {
        try {
            FileWriter writer = new FileWriter(path);

            writer.write(object.toString());
            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }
}
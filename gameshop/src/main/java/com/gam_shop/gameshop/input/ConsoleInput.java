package com.gam_shop.gameshop.input;

import org.springframework.stereotype.Component;
import com.gam_shop.gameshop.interfaces.Input;

import java.util.Scanner;



@Component
public class ConsoleInput implements Input {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getString() {
        return scanner.nextLine();
    }

    public double getDouble() {
        return Double.parseDouble(scanner.nextLine());
    }

    public int getInt() {
        return Integer.parseInt(scanner.nextLine());
    }

}

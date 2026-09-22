package com.zfettostudios.zfuctone.util;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.logging.DispatchType;
import com.zfettostudios.zfuctone.logging.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleLogic {
    private static Logger logger;

    public static void run(String[] args) {
        Scanner scanner = new Scanner(System.in);
        logger = ZFuctone.getInstance().logger();

        label:
        while (true) {
            logger.info(DispatchType.FORMAT_PRINT, ">");
            String[] lineArgs = scanner.nextLine().trim().split("\\s+");

            switch (lineArgs[0]) {
                case "" -> {}
                case "exit", "0" -> {
                    ZFuctone.getInstance().disable();
                    break label;
                }
                case "help" -> printHelp();
                case "to_byte" -> toByte(lineArgs);
                case "from_byte" -> fromByte(lineArgs);
                default -> invalidCommand(lineArgs);
            }
        }
    }

    private static void invalidCommand(String[] lineArgs) {
        logger.info("<red>\"", Utils.join(lineArgs), "\" неверная команда!");
        logger.info("<red>Введите <yellow>help</yellow>, чтобы получить список доступных команд");
    }

    private static void printHelp() {
        logger.info();
    }

    private static void fromByte(String[] args) {
        if (args.length < 2) {
            logger.info("<red>Не введены байты!<red>");
            return;
        }

        try {
            byte[] bytes = new byte[args.length - 1];

            for (int i = 1; i < args.length; i++) {
                String clean = args[i].replaceAll("[\\[\\],]", "").trim();
                if (clean.isEmpty()) continue;

                boolean isHex = clean.startsWith("0x");
                String rawNumber = isHex ? clean.substring(2) : clean;
                int radix = isHex ? 16 : 10;

                bytes[i - 1] = (byte) Integer.parseInt(rawNumber, radix);
            }

            logger.info(new String(bytes, StandardCharsets.UTF_8));
        } catch (NumberFormatException e) {
            logger.info("<red>Ошибка парсинга байтов!</red>");
            logger.info("<red>", e.getMessage(), "</red>");
        }
    }
    private static void toByte(String[] args) {
        if (args.length < 2) {
            logger.info("<red>Не введен текст!</red>");
            return;
        }

        byte[] bytes = Arrays.stream(args)
            .skip(1)
            .collect(Collectors.joining(" "))
            .getBytes(StandardCharsets.UTF_8);

        logger.info(Arrays.toString(bytes).replaceAll("[\\[\\]]", ""));
    }
}

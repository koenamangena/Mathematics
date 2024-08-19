package com.koena.mathematics.service;

import com.google.gson.Gson;
import com.koena.mathematics.domain.States;
import com.koena.mathematics.models.StatesDto;
import com.koena.mathematics.repository.StatesRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InterpreterService {

    private static final String SECRET_KEY = "1234567890123456";
    double result = 0.0;
    @Autowired
    private StatesRepository statesRepository;
    private final Map<String, Double> variables = new HashMap<>();

    @SneakyThrows
    private byte[] encryptState(Map<String, Double> variables) {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        String json = new Gson().toJson(variables);
        return cipher.doFinal(json.getBytes());
    }

    @SneakyThrows
    public Map<String, Double> decryptState(byte[] state) {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        String json = new String(cipher.doFinal(state));
        return new Gson().fromJson(json, HashMap.class);
    }

    public List<StatesDto> getState(String user, LocalDateTime start, LocalDateTime end) {
        return statesRepository.findByUserAndStartEqualsAndEndEquals(user, start, end)
                .stream()
                .map(s ->
                        StatesDto.builder()
                                .id(s.getId())
                                .start(s.getStart())
                                .end(s.getEnd())
                                .user(s.getUser())
                                .states(decryptState(s.getStates()))
                                .build()
                ).collect(Collectors.toList());
    }

    public StatesDto evaluate(String expression, String user) {
        String[] tokens = expression.split(" ");

        if (tokens.length == 3 && tokens[1].equals("=")) {
            String variable = tokens[0];
            double value = Double.parseDouble(tokens[2]);
            variables.put(variable, value);
            result = value;
        } else {
            result = evaluateExpression(expression);
            String variable = tokens[0];
            variables.put(variable, result);
        }

        States state = new States();
        state.setUser(user);
        state.setStates(encryptState(variables));
        log.info("sate start time: %s and endTime %s for user: %s".formatted(state.getStart(), state.getEnd(), state.getUser()));
        statesRepository.save(state);
        return StatesDto.builder()
                .id(state.getId())
                .end(state.getEnd())
                .start(state.getStart())
                .user(state.getUser())
                .states(decryptState(state.getStates()))
                .build();
    }

    private double evaluateExpression(String expr) {
        String[] tokens = expr.split(" ");

        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double operand = Double.parseDouble(tokens[i + 1]);

            switch (operator) {
                case "+":
                    result += operand;
                    break;
                case "-":
                    result -= operand;
                    break;
                case "*":
                    result *= operand;
                    break;
                case "/":
                    result /= operand;
                    break;
                case "%":
                    result %= operand;
                    break;
            }
        }

        return result;
    }
}


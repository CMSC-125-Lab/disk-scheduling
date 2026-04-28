package ui;

import java.util.ArrayList;
import java.util.List;

import simulation.SimulationEngine;

public final class InputValidators {
    private InputValidators() {
    }

    public static int[] parseQueue(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Queue cannot be empty.");
        }

        String[] tokens = raw.split(",");
        if (tokens.length > 40) {
            throw new IllegalArgumentException("Queue can contain at most 40 values.");
        }

        List<Integer> values = new ArrayList<>();
        for (String token : tokens) {
            String trimmed = token.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            int value;
            try {
                value = Integer.parseInt(trimmed);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Queue contains non-numeric values.");
            }
            validateCylinder(value, "Queue value");
            values.add(value);
        }

        if (values.isEmpty()) {
            throw new IllegalArgumentException("Queue cannot be empty.");
        }

        int[] queue = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            queue[i] = values.get(i);
        }
        return queue;
    }

    public static int parseHead(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Head start value is required.");
        }

        int head;
        try {
            head = Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Head start must be an integer.");
        }

        validateCylinder(head, "Head start");
        return head;
    }

    private static void validateCylinder(int value, String label) {
        if (value < SimulationEngine.MIN_CYLINDER || value > SimulationEngine.MAX_CYLINDER) {
            throw new IllegalArgumentException(label + " must be between 0 and 199.");
        }
    }
}

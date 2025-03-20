package pl.sudoku;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public enum Difficulty {
        EASY(10),
        MEDIUM(20),
        HARD(30);

        private final int amountToDelete;

        Difficulty(int amountToDelete) {
            this.amountToDelete = amountToDelete;
        }

        public void clearFields(SudokuBoard board) {
            Random rand = new Random();
            Set<Integer> wylosowanePola = new HashSet<>();
            int usunietePola = 0;

            while (usunietePola < amountToDelete) {
                int wiersz = rand.nextInt(9);
                int kolumna = rand.nextInt(9);

                int indeks = wiersz * 9 + kolumna;

                // Sprawdzamy, czy pole nie zostało już wcześniej usunięte
                if (!wylosowanePola.contains(indeks)) {
                    board.set(wiersz,kolumna,0);
                    //board.addDeletedCoordinates(wiersz,kolumna);
                    wylosowanePola.add(indeks);
                    usunietePola++;
                }
            }

        }

}

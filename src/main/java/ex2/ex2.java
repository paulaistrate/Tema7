package ex2;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ex2 {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Set<InstrumentMuzical> instrumente = new HashSet<>();
        // 1. Introducerea instrumentelor
        instrumente.add(new Chitara("Yamaha", 2500, Chitara.TipChitara.ACUSTICA, 6));
        instrumente.add(new Chitara("Fender", 3000, Chitara.TipChitara.ELECTRICA, 7));
        instrumente.add(new Chitara("Gibson", 4000, Chitara.TipChitara.CLASICA, 6));
        instrumente.add(new SetTobe("Roland", 4500, SetTobe.TipTobe.ELECTRONICE, 5, 2));
        instrumente.add(new SetTobe("Pearl", 3800, SetTobe.TipTobe.ACUSTICE, 7, 3));
        instrumente.add(new SetTobe("Yamaha", 2500, SetTobe.TipTobe.ACUSTICE, 6, 4));

        // 2. Salvarea colecției în JSON
        try {
            mapper.writeValue(new File("instrumente.json"), instrumente);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Citirea colecției din JSON
        Set<InstrumentMuzical> instrumenteCitite;
        try {
            instrumenteCitite = mapper.readValue(
                    new File("instrumente.json"),
                    new TypeReference<Set<InstrumentMuzical>>() {}
            );

            // 4. Afișarea implementării Set
            System.out.println("Implementarea Set utilizată: " + instrumenteCitite.getClass().getName());

            // 5. Verificarea duplicate
            Chitara chitaraDuplicata = new Chitara("Yamaha", 2500, Chitara.TipChitara.ACUSTICA, 6);
            if (!instrumenteCitite.add(chitaraDuplicata)) {
                System.out.println("Duplicatele nu sunt permise în Set.");
            }

            // 6. Ștergerea instrumentelor cu preț > 3000
            instrumenteCitite.removeIf(instr -> instr.getPret() > 3000);

            // 7. Afișarea chitarelor
            System.out.println("\nChitarele:");
            instrumenteCitite.stream()
                    .filter(instr -> instr instanceof Chitara)
                    .forEach(System.out::println);

            // 8. Afișarea seturilor de tobe
            System.out.println("\nSeturile de tobe:");
            instrumenteCitite.stream()
                    .filter(instr -> instr.getClass() == SetTobe.class)
                    .forEach(System.out::println);

            // 9. Chitara cu cele mai multe corzi
            System.out.println("\nChitara cu cele mai multe corzi:");
            Optional<Chitara> chitaraMax = instrumenteCitite.stream()
                    .filter(instr -> instr instanceof Chitara)
                    .map(instr -> (Chitara) instr)
                    .max((c1, c2) -> Integer.compare(c1.getNrCorzi(), c2.getNrCorzi()));
            chitaraMax.ifPresent(System.out::println);

            // 10. Tobe acustice ordonate după numărul de tobe
            System.out.println("\nSeturi de tobe acustice ordonate după numărul de tobe:");
            instrumenteCitite.stream()
                    .filter(instr -> instr instanceof SetTobe)
                    .map(instr -> (SetTobe) instr)
                    .filter(tobe -> tobe.getTipTobe() == SetTobe.TipTobe.ACUSTICE)
                    .sorted((t1, t2) -> Integer.compare(t1.getNrTobe(), t2.getNrTobe()))
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

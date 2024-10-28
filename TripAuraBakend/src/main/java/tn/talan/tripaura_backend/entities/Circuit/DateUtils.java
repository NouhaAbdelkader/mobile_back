package tn.talan.tripaura_backend.entities.Circuit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
public class DateUtils {

    // Convertir LocalDate en Date avec le début de la journée
    public static Date toStartOfDay(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Convertir LocalDate en Date avec la fin de la journée
    public static Date toEndOfDay(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Convertir LocalDate en Date avec début et fin de journée
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

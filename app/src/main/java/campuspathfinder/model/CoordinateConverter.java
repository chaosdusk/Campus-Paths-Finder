package campuspathfinder.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class CoordinateConverter extends AbstractBeanField<String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            String[] parts = value.split(",");
            Coordinate coordinate = new Coordinate();
            coordinate.setX(Double.parseDouble(parts[0]));
            coordinate.setY(Double.parseDouble(parts[1]));
            return coordinate;
        } catch (RuntimeException e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}
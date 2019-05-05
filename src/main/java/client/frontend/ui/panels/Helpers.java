package client.frontend.ui.panels;

import com.github.lgooddatepicker.components.DatePickerSettings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Helpers {
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy");
  public static DatePickerSettings getStandardDatePickerSettings() {
    DatePickerSettings settings = new DatePickerSettings();
    settings.setAllowKeyboardEditing(false);
    return settings;
  }

  private Helpers() {
  }
}

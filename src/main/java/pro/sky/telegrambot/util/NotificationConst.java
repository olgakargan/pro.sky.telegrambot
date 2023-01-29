package pro.sky.telegrambot.util;

import lombok.experimental.UtilityClass;
@UtilityClass
public class NotificationConst {
    public static final String FOUND_NOTIFICATIONS = "<b>Найденные напоминания: </b>";
    public static final String NOT_FOUND = "Напоминаний не найдено";
    public static final String SAVED_OK = "Напоминание успешно создано";
    public static final String SAVED_FAIL = "Не удалось сохранить напоминание по причине ";
    public static final String EDIT_OK = "Напоминание успешно изменено";
    public static final String EDIT_FAIL = "Не удалось изменить напоминание по причине ";
    public static final String DELETE_OK = "Напоминание успешно удалено";
    public static final String DELETE_FAIL = "Не удалось удалить напоминание по причине ";
    public static final String WRONG_ID = "Неправильный номер напоминанания! Введите номер заново\n" +
            " или отмените действия кнопкой 'Отменить'";
    public static final String WRONG_DATE = "Неправильно введена дата! Дата должна быть " +
            " в формате dd.mm.yyyy. Введите дату заново или\n" +
            " отмените действия кнопкой 'Отменить'";
    public static final String WRONG_TIME = "Неправильно введено время! Время должно быть " +
            "в формате hh:mm. Введите время заново\n" +
            " или отмените действия кнопкой 'Отменить'";
    public static final String ENTER_ID = "Введите номер напоминания";
    public static final String ENTER_DATE = "Введите дату";
    public static final String ENTER_TIME = "Введите время";
    public static final String ENTER_TEXT = "Введите текст напоминания";
    public static final String ENTER_PART = "Введите часть текста напоминания";
}
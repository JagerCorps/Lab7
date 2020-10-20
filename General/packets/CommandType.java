package packets;

import java.io.Serializable;

/**
 * Enum-класс типов команд
 */
public enum CommandType implements Serializable {
    add,
    add_if_max,
    clear,
    execute_script,
    exit,
    filter_less_than_annual_turnover,
    help,
    info,
    print_field_descending_annual_turnover,
    remove_all_by_full_name,
    remove_by_id,
    remove_first,
    remove_lower,
    show,
    update,
    null_command,
}

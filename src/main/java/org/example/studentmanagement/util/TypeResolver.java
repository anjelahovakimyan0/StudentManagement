package org.example.studentmanagement.util;

import lombok.experimental.UtilityClass;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;

@UtilityClass
public class TypeResolver {

    public static String resolveType(User user) {
        if (user.getUserType() == UserType.TEACHER) {
            return "redirect:/users/teachers";
        }
        return "redirect:/users/students";
    }
}

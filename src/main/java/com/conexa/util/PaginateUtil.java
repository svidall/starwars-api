package com.conexa.util;

import com.conexa.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

public class PaginateUtil {
    public static String getUri(HttpServletRequest httpServletRequest, int page, int limit, String propertyName, String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(httpServletRequest.getRequestURI())
                .queryParam("page", page)
                .queryParam("limit", limit);
        if (name != null && !name.isBlank()) {
            builder.queryParam(propertyName, name);
        }
        return builder.toUriString();
    }

    public static Pagination getPagination(HttpServletRequest httpServletRequest, int page, int limit, String title, String propertyName, int totalRecords) {
        Pagination pagination = new Pagination();
        int recordIndex = page * limit - limit;
        pagination.setTotalRecords(totalRecords);
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be greater than 0");
        }
        if (page > 1) {
            pagination.setPrevious(PaginateUtil.getUri(httpServletRequest, page - 1, limit, propertyName, title));
        }
        if (recordIndex + limit < totalRecords) {
            pagination.setNext(PaginateUtil.getUri(httpServletRequest, page + 1, limit, propertyName, title));
        }
        int totalPages = (totalRecords + limit - 1) / limit;
        pagination.setTotalPages(totalPages);
        return pagination;
    }
}

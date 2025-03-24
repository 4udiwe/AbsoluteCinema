package com.example.data.mapper

/**
 * Интерфейс [Mapper] реализуют мапперы DtoToEntity и EntityToDomain.
 * Преобразует сущности фильмов (Movie)
 *
 *
 * @param T тип преобразуемой сущности.
 * @param R тип сущности на выходе
 */
interface MovieMapper<in T, out R> {
    /**
     * Преобразует сущности:
     * DTO в Entity,
     * Entity в Domain
     *
     * @param movie сущность, подлежащая преобразованию
     * @return сущность другого типа
     */
    fun map(movie: T) : R
}
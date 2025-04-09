package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.data.local.entity.MovieEntity
import com.example.data.local.entity.category.CategoryEntity
import com.example.data.local.entity.category.MovieCategoryCrossRef

@Dao
interface CategoryDao {

    @Query("""
    SELECT MovieEntity.* 
    FROM MovieEntity
    INNER JOIN MovieCategoryCrossRef ON MovieEntity.id = MovieCategoryCrossRef.movieId
    INNER JOIN CategoryEntity ON MovieCategoryCrossRef.categoryId = CategoryEntity.id
    WHERE CategoryEntity.name = :category
""")
    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>>

    @Query("""
        SELECT CategoryEntity.*
        FROM CategoryEntity
        INNER JOIN MovieCategoryCrossRef on CategoryEntity.id = categoryId
        WHERE movieId = :movieId
    """)
    suspend fun getCategoriesForMovie(movieId: Int) : List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(categoryEntity: CategoryEntity)

    @Insert
    suspend fun addCategoryToMovie(movieCategoryCrossRef: MovieCategoryCrossRef)

    @Query("""
    DELETE 
    FROM MovieCategoryCrossRef
    WHERE movieId = :movieId AND categoryId IN (
        SELECT CategoryEntity.id
        FROM CategoryEntity
        WHERE CategoryEntity.name = :category
    )
""")
    suspend fun deleteCategoryFromMovie(movieId: Int, category: String)
}
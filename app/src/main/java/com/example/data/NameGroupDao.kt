package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NameGroupDao {
    @Query("SELECT * FROM name_groups ORDER BY createdAt DESC")
    fun getAllGroups(): Flow<List<NameGroupEntity>>

    @Query("SELECT * FROM name_group_members WHERE groupId = :groupId ORDER BY sortOrder ASC")
    suspend fun getMembersForGroup(groupId: Long): List<NameGroupMemberEntity>

    @Insert
    suspend fun insertGroup(group: NameGroupEntity): Long

    @Insert
    suspend fun insertMembers(members: List<NameGroupMemberEntity>)

    @Delete
    suspend fun deleteGroup(group: NameGroupEntity)

    @Query("DELETE FROM name_group_members WHERE groupId = :groupId")
    suspend fun deleteMembersForGroup(groupId: Long)
}

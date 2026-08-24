package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AnnouncementRecordEntity
import com.example.data.model.AssetRwEntity
import com.example.data.model.CashTransactionEntity
import com.example.data.model.CommunityEventEntity
import com.example.data.model.ComplaintRecordEntity
import com.example.data.model.DuesRecordEntity
import com.example.data.model.EmergencyAlertEntity
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.IncidentRecordEntity
import com.example.data.model.LetterRequestEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfficerMemberEntity
import com.example.data.model.PollingEntity
import com.example.data.model.ResidentDirectoryEntity
import com.example.data.model.ResidentProfileEntity
import com.example.data.model.RondaScheduleEntity
import com.example.data.model.SocialHelpEntity
import com.example.data.model.UserParticipationEntity

@Database(
    entities = [
        ResidentProfileEntity::class,
        FamilyMemberEntity::class,
        LetterRequestEntity::class,
        DuesRecordEntity::class,
        ComplaintRecordEntity::class,
        AnnouncementRecordEntity::class,
        NotificationEntity::class,
        RondaScheduleEntity::class,
        EmergencyAlertEntity::class,
        CashTransactionEntity::class,
        CommunityEventEntity::class,
        PollingEntity::class,
        SocialHelpEntity::class,
        IncidentRecordEntity::class,
        UserParticipationEntity::class,
        OfficerMemberEntity::class,
        ResidentDirectoryEntity::class,
        AssetRwEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun residentProfileDao(): ResidentProfileDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun letterRequestDao(): LetterRequestDao
    abstract fun duesRecordDao(): DuesRecordDao
    abstract fun complaintRecordDao(): ComplaintRecordDao
    abstract fun announcementRecordDao(): AnnouncementRecordDao
    abstract fun notificationDao(): NotificationDao
    abstract fun rondaScheduleDao(): RondaScheduleDao
    abstract fun emergencyAlertDao(): EmergencyAlertDao
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun communityEventDao(): CommunityEventDao
    abstract fun pollingDao(): PollingDao
    abstract fun socialHelpDao(): SocialHelpDao
    abstract fun incidentRecordDao(): IncidentRecordDao
    abstract fun userParticipationDao(): UserParticipationDao
    abstract fun officerDao(): OfficerDao
    abstract fun residentDirectoryDao(): ResidentDirectoryDao
    abstract fun assetRwDao(): AssetRwDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rtrw_warga.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

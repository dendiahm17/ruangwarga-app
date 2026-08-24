package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface ResidentProfileDao {
    @Query("SELECT * FROM resident_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<ResidentProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: ResidentProfileEntity)

    @Query("DELETE FROM resident_profile")
    suspend fun deleteProfile()
}

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members ORDER BY id ASC")
    fun getFamilyMembers(): Flow<List<FamilyMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<FamilyMemberEntity>)

    @Query("DELETE FROM family_members WHERE id = :id")
    suspend fun deleteMember(id: Int)
}

@Dao
interface LetterRequestDao {
    @Query("SELECT * FROM letter_requests ORDER BY id DESC")
    fun getAllLetters(): Flow<List<LetterRequestEntity>>

    @Query("SELECT * FROM letter_requests WHERE status = :status ORDER BY id DESC")
    fun getLettersByStatus(status: String): Flow<List<LetterRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetter(letter: LetterRequestEntity): Long

    @Update
    suspend fun updateLetter(letter: LetterRequestEntity)

    @Query("UPDATE letter_requests SET status = :status, catatanRt = :catatanRt, tanggalSelesai = :tanggalSelesai WHERE id = :id")
    suspend fun updateLetterStatus(id: Int, status: String, catatanRt: String?, tanggalSelesai: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetters(letters: List<LetterRequestEntity>)
}

@Dao
interface DuesRecordDao {
    @Query("SELECT * FROM dues_records ORDER BY id DESC")
    fun getAllDues(): Flow<List<DuesRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDues(dues: DuesRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDuesList(duesList: List<DuesRecordEntity>)

    @Update
    suspend fun updateDues(dues: DuesRecordEntity)

    @Query("UPDATE dues_records SET status = :status, tanggalBayar = :tanggalBayar WHERE id = :id")
    suspend fun updateDuesStatus(id: Int, status: String, tanggalBayar: String?)
}

@Dao
interface ComplaintRecordDao {
    @Query("SELECT * FROM complaint_records ORDER BY id DESC")
    fun getAllComplaints(): Flow<List<ComplaintRecordEntity>>

    @Query("SELECT * FROM complaint_records WHERE status = :status ORDER BY id DESC")
    fun getComplaintsByStatus(status: String): Flow<List<ComplaintRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaints(complaints: List<ComplaintRecordEntity>)

    @Query("UPDATE complaint_records SET status = :status, tanggapanRt = :tanggapanRt WHERE id = :id")
    suspend fun updateComplaintStatus(id: Int, status: String, tanggapanRt: String?)
}

@Dao
interface AnnouncementRecordDao {
    @Query("SELECT * FROM announcement_records ORDER BY id DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<AnnouncementRecordEntity>)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isDibaca = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isDibaca = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("UPDATE notifications SET isDibaca = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
}

@Dao
interface RondaScheduleDao {
    @Query("SELECT * FROM ronda_schedules ORDER BY id ASC")
    fun getAllSchedules(): Flow<List<RondaScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<RondaScheduleEntity>)

    @Update
    suspend fun updateSchedule(schedule: RondaScheduleEntity)

    @Query("UPDATE ronda_schedules SET statusKehadiranSaya = :status WHERE id = :id")
    suspend fun updateAttendance(id: Int, status: String)
}

@Dao
interface EmergencyAlertDao {
    @Query("SELECT * FROM emergency_alerts ORDER BY id DESC")
    fun getAllAlerts(): Flow<List<EmergencyAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: EmergencyAlertEntity): Long

    @Query("UPDATE emergency_alerts SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)
}

@Dao
interface CashTransactionDao {
    @Query("SELECT * FROM cash_transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<CashTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CashTransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<CashTransactionEntity>)

    @Query("DELETE FROM cash_transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Int)
}

@Dao
interface CommunityEventDao {
    @Query("SELECT * FROM community_events ORDER BY id ASC")
    fun getAllEvents(): Flow<List<CommunityEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CommunityEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CommunityEventEntity>)

    @Query("UPDATE community_events SET rsvpStatus = :status, jumlahHadir = :jumlahHadir WHERE id = :id")
    suspend fun updateRsvp(id: Int, status: String, jumlahHadir: Int)

    @Query("UPDATE community_events SET partisipasiStatus = :partisipasiStatus, jumlahHadir = :jumlahHadir WHERE id = :id")
    suspend fun updatePartisipasi(id: Int, partisipasiStatus: String, jumlahHadir: Int)

    @Query("UPDATE community_events SET terpenuhiRelawan = :relawan, terpenuhiSapu = :sapu, terpenuhiEmber = :ember, terpenuhiPickup = :pickup, partisipasiStatus = :partisipasiStatus WHERE id = :id")
    suspend fun updateLogistics(id: Int, relawan: Int, sapu: Int, ember: Int, pickup: Int, partisipasiStatus: String)
}

@Dao
interface SocialHelpDao {
    @Query("SELECT * FROM social_help_records ORDER BY id DESC")
    fun getAllSocialHelp(): Flow<List<SocialHelpEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialHelp(item: SocialHelpEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialHelpList(items: List<SocialHelpEntity>)

    @Query("UPDATE social_help_records SET status = :status, jumlahRelawan = :jumlahRelawan, isMyContributed = :isContributed WHERE id = :id")
    suspend fun updateContribution(id: Int, status: String, jumlahRelawan: Int, isContributed: Boolean)
}

@Dao
interface IncidentRecordDao {
    @Query("SELECT * FROM incident_records ORDER BY id DESC")
    fun getAllIncidents(): Flow<List<IncidentRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncidents(incidents: List<IncidentRecordEntity>)

    @Query("UPDATE incident_records SET status = :status, waktuPerbaikan = :waktuPerbaikan, waktuSelesai = :waktuSelesai, catatanPengurus = :catatan WHERE id = :id")
    suspend fun updateIncidentStatus(id: Int, status: String, waktuPerbaikan: String?, waktuSelesai: String?, catatan: String)
}

@Dao
interface UserParticipationDao {
    @Query("SELECT * FROM user_participations ORDER BY id DESC")
    fun getAllParticipations(): Flow<List<UserParticipationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipation(participation: UserParticipationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipations(participations: List<UserParticipationEntity>)
}

@Dao
interface PollingDao {
    @Query("SELECT * FROM polling_records ORDER BY id DESC")
    fun getAllPolls(): Flow<List<PollingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoll(poll: PollingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolls(polls: List<PollingEntity>)

    @Query("UPDATE polling_records SET suaraA = :suaraA, suaraB = :suaraB, suaraC = :suaraC, myVote = :myVote WHERE id = :id")
    suspend fun recordVote(id: Int, suaraA: Int, suaraB: Int, suaraC: Int, myVote: String)

    @Query("UPDATE polling_records SET status = :status WHERE id = :id")
    suspend fun updatePollStatus(id: Int, status: String)
}

@Dao
interface OfficerDao {
    @Query("SELECT * FROM officer_members ORDER BY tier ASC, id ASC")
    fun getAllOfficers(): Flow<List<OfficerMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfficer(officer: OfficerMemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfficers(officers: List<OfficerMemberEntity>)

    @Query("UPDATE officer_members SET isOnline = :isOnline, statusText = :statusText WHERE id = :id")
    suspend fun updateOnlineStatus(id: Int, isOnline: Boolean, statusText: String)
}

@Dao
interface ResidentDirectoryDao {
    @Query("SELECT * FROM resident_directory ORDER BY isOnline DESC, nama ASC")
    fun getAllResidents(): Flow<List<ResidentDirectoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResident(resident: ResidentDirectoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResidents(residents: List<ResidentDirectoryEntity>)

    @Query("UPDATE resident_directory SET isOnline = :isOnline, statusText = :statusText WHERE id = :id")
    suspend fun updateOnlineStatus(id: Int, isOnline: Boolean, statusText: String)
}

@Dao
interface AssetRwDao {
    @Query("SELECT * FROM asset_rw ORDER BY id ASC")
    fun getAllAssets(): Flow<List<AssetRwEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetRwEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<AssetRwEntity>)

    @Update
    suspend fun updateAsset(asset: AssetRwEntity)

    @Query("UPDATE asset_rw SET statusKetersediaan = :status WHERE id = :id")
    suspend fun updateAssetAvailability(id: Int, status: String)

    @Query("DELETE FROM asset_rw WHERE id = :id")
    suspend fun deleteAsset(id: Int)
}




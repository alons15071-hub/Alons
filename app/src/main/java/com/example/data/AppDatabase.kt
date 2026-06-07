package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// 1. ROOM ENTITIES
// ==========================================

@Entity(tableName = "service_requests")
data class ServiceRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val port: String,               // Callao, Chancay, Paita, Matarani, Pisco
    val targetVessel: String,       // e.g. "MV Antares C"
    val serviceType: String,        // Crew Transfer, Provisioning, Technical Spare, Inspector
    val dateTime: String,           // Date time string
    val status: String,             // Pendiente, Aprobado, En Ruta, Completado
    val requestedBy: String,        // e.g. "Cosmos Shipping Agency"
    val notes: String = "",
    val launchName: String = "",    // "Lancha Orca V", "Lancha Delfin I"
    val latitude: Double = -12.04637,
    val longitude: Double = -77.14234,
    val price: Double = 350.0
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "documents")
data class DocumentItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val filename: String,
    val category: String,          // Certificado, Permiso, Matricula, Poliza
    val uploadDate: String,
    val expiryDate: String,
    val status: String,            // Vigente, Por Vencer, Vencido
    val size: String
)

// ==========================================
// 2. DATA ACCESS OBJECT (DAO)
// ==========================================

@Dao
interface SmartLaunchDao {
    // Service Requests
    @Query("SELECT * FROM service_requests ORDER BY id DESC")
    fun getAllRequests(): Flow<List<ServiceRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: ServiceRequest): Long

    @Update
    suspend fun updateRequest(request: ServiceRequest)

    @Query("DELETE FROM service_requests WHERE id = :id")
    suspend fun deleteRequestById(id: Int)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Int)

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

    // Documents
    @Query("SELECT * FROM documents ORDER BY uploadDate DESC")
    fun getAllDocuments(): Flow<List<DocumentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentItem)

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteDocumentById(id: Int)
}

// ==========================================
// 3. DATABASE CLASS
// ==========================================

@Database(
    entities = [ServiceRequest::class, NotificationItem::class, DocumentItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): SmartLaunchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_launch_db"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

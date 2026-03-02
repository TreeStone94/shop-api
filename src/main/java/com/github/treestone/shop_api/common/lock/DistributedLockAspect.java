package com.github.treestone.shop_api.common.lock;

import com.github.treestone.shop_api.common.exception.LockAcquisitionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
	private final RedissonClient redissonClient;

	@Around("@annotation(distributedLock)")
	public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
		// 1. SpEL로 key 파싱
		String lockKey = parseLockKey(distributedLock.key(), joinPoint);

		// 2. Redisson 락 객체 생성
		RLock lock = redissonClient.getLock(lockKey);

		try {

			// 3. 락 획득 시도
			boolean acquired = lock.tryLock(
					distributedLock.waitTime(),
					distributedLock.leaseTime(),
					distributedLock.timeUnit()
			);

			if (!acquired) {
				log.warn("Failed to acquire lock: {}", lockKey);
				throw new LockAcquisitionException("잠시 후 다시 시도해주세요");
			}

			log.info("Lock acquired: {}", lockKey);

			// 4. 비즈니스 로직 실행
			return joinPoint.proceed();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Interrupted while acquiring lock: {}", lockKey, e);
			throw new LockAcquisitionException("락 획득 중 인터럽트 발생");
		} finally {
			// 5. 락 해제 (소유자만 해제)
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.info("Lock released: {}", lockKey);
			}
			lock.unlock();
		}
	}

	private String parseLockKey(String keyExpression, ProceedingJoinPoint joinPoint) {
		// 임시로 간단한 구현 (다음 단계에서 SpEL 파싱 추가)
		return keyExpression;
	}
}
